package me.injent.myschool.core.data.repository

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import me.injent.myschool.core.common.network.Dispatcher
import me.injent.myschool.core.common.network.MsDispatchers.IO
import me.injent.myschool.core.common.util.atTimeZone
import me.injent.myschool.core.data.model.asEntity
import me.injent.myschool.core.data.util.RepoDependency
import me.injent.myschool.core.database.dao.MarkDao
import me.injent.myschool.core.database.dao.PersonDao
import me.injent.myschool.core.database.dao.SubjectDao
import me.injent.myschool.core.database.model.MarkEntity
import me.injent.myschool.core.database.model.SubjectEntity
import me.injent.myschool.core.database.model.asExternalModel
import me.injent.myschool.core.model.*
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.NetworkMark
import me.injent.myschool.core.network.model.asExternalModel
import javax.inject.Inject
import kotlin.math.roundToInt

private const val MAX_MARKS_PERSUBJECT = 100

@RepoDependency(UserContextRepository::class, SubjectRepository::class)
interface MarkRepository : Syncable {
    suspend fun getPersonFinalMarkBySubject(
        personId: Long, subjectId: Long, period: Period
    ): Float
    suspend fun getPersonMarksBySubject(
        personId: Long,
        subjectId: Long,
        period: Period
    ): List<Mark>
    suspend fun getPersonAverageMarkValue(
        personId: Long,
        dateStart: LocalDateTime,
        dateFinish: LocalDateTime
    ): Float
    suspend fun receiveNewMarks(): ReceivedMarksResult
    suspend fun receiveClassmatesMarks(): Boolean
    fun getMark(markId: Long): Flow<Mark>
    suspend fun getPersonMarksByPeriod(personId: Long, schoolId: Long, period: Period): List<Mark>
    suspend fun getMarkDetails(
        personId: Long,
        groupId: Long,
        markId: Long
    ): MarkDetails

    suspend fun getRecentMarks(
        personId: Long,
        groupId: Long,
        fromDate: LocalDateTime? = null,
        subjectId: Long? = null,
        limit: Int = MAX_MARKS_PERSUBJECT
    ): RecentMarks

    suspend fun downloadAllMarksByPeriod(periodId: Long): Boolean
}

sealed interface ReceivedMarksResult {
    object NotChanged : ReceivedMarksResult
    data class NewMark(
        val value: String,
        val subjectName: String,
        val markId: Long
    ) : ReceivedMarksResult
    data class MultipleMarks(val marks: List<NewMark>) : ReceivedMarksResult
    object Error : ReceivedMarksResult
}

class OfflineFirstMarkRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val subjectDao: SubjectDao,
    private val markDao: MarkDao,
    private val userContextRepository: UserContextRepository,
    private val userDataRepository: UserDataRepository,
    private val personDao: PersonDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : MarkRepository {

    override suspend fun synchronize(onProgress: ((Int) -> Unit)?): Boolean = try {
        var progress = 0
        val groupId: Long
        val periods: List<Period>
        userContextRepository.userContext.first()!!.run {
            groupId = group.id
            periods = reportingPeriodGroup.periods
        }

        for (period in periods) {
            val subjectIds = subjectDao.getSubjects().first().map(SubjectEntity::id)
            for (subjectId in subjectIds) {
                val marks = networkDataSource.getEduGroupMarksBySubject(
                    groupId,
                    subjectId,
                    period.dateStart,
                    period.dateFinish
                )
                markDao.saveMarks(marks.map { it.asEntity(subjectId) })
                progress++
                val percentage =
                    (((progress / 2f) / subjectIds.size) * 100)
                        .roundToInt()
                        .coerceIn(0, 100)
                onProgress?.invoke(percentage)
            }
        }

        true
    } catch (e: Exception) {
        Log.e("MarkRepository", "Failed to sync")
        e.printStackTrace()
        false
    }

    override suspend fun downloadAllMarksByPeriod(periodId: Long): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun receiveNewMarks(): ReceivedMarksResult {
        try {
            val personId: Long
            val schoolId: Long
            val dateStart: LocalDateTime
            val dateEnd: LocalDateTime

            userContextRepository.userContext.first()!!.run {
                personId = this.personId
                schoolId = this.school.id
                reportingPeriodGroup.periods.find(Period::isCurrent)!!.run {
                    dateStart = userDataRepository.userData.first().lastMarksSyncDateTime
                        ?: this.dateStart
                    dateEnd = this.dateFinish
                }
            }

            val markEntitiesToSave = mutableListOf<MarkEntity>()

            val marks = networkDataSource.getPersonMarksByPeriod(
                personId = personId,
                schoolId = schoolId,
                from = dateStart,
                to = dateEnd
            )
                .filterNot { markDao.contains(it.id) }
                .map { mark ->
                    val subject = networkDataSource.getLesson(mark.lessonId!!).subject!!
                    markEntitiesToSave.add(mark.asEntity(subject.id))

                    ReceivedMarksResult.NewMark(
                        value = mark.value,
                        markId = mark.id,
                        subjectName = subject.name
                    )
                }

            userDataRepository.updateMarksSyncDateTime()
            return when {
                marks.size > 1 -> {
                    markDao.saveMarks(markEntitiesToSave)
                    ReceivedMarksResult.MultipleMarks(marks)
                }
                marks.size == 1 -> {
                    markDao.saveMark(markEntitiesToSave.single())

                    val mark = marks.single()
                    ReceivedMarksResult.NewMark(
                        value = mark.value,
                        subjectName = mark.subjectName,
                        markId = mark.markId
                    )
                }
                else -> ReceivedMarksResult.NotChanged
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ReceivedMarksResult.Error
        }
    }

    override suspend fun receiveClassmatesMarks(): Boolean = try {
        val myPersonId: Long
        val schoolId: Long
        val period: Period
        userContextRepository.userContext.first()!!.run {
            myPersonId = this.personId
            schoolId = this.school.id
            period = this.reportingPeriodGroup.periods.find(Period::isCurrent)!!
        }

        for (personId in personDao.getClassmatesPersonIds(myPersonId)) {
            val marks = networkDataSource.getPersonMarksByPeriod(
                personId = personId,
                schoolId = schoolId,
                from = userDataRepository.userData.first().lastMarksSyncDateTime
                    ?: period.dateStart,
                to = period.dateFinish
            ).filterNot { markDao.contains(it.id) }

            marks.forEach { mark ->
                val subjectId = networkDataSource.getLesson(mark.lessonId!!).subject!!.id
                markDao.saveMark(mark.asEntity(subjectId))
            }
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    override fun getMark(markId: Long): Flow<Mark> =
        markDao.getMark(markId).map(MarkEntity::asExternalModel)

    override suspend fun getPersonMarksByPeriod(personId: Long, schoolId: Long, period: Period): List<Mark> {
        return networkDataSource.getPersonMarksByPeriod(
            personId = personId,
            schoolId = schoolId,
            from = period.dateStart.atTimeZone(TimeZone.UTC),
            to = period.dateFinish.atTimeZone(TimeZone.UTC)
        ).map(NetworkMark::asExternalModel)
    }

    override suspend fun getMarkDetails(personId: Long, groupId: Long, markId: Long): MarkDetails =
        networkDataSource.getMarkDetails(personId, groupId, markId).asExternalModel()

    override suspend fun getRecentMarks(
        personId: Long,
        groupId: Long,
        fromDate: LocalDateTime?,
        subjectId: Long?,
        limit: Int
    ): RecentMarks =
        networkDataSource.getRecentMarks(
            personId, groupId, fromDate, subjectId, limit
        ).asExternalModel()

    override suspend fun getPersonFinalMarkBySubject(
        personId: Long,
        subjectId: Long,
        period: Period
    ): Float = markDao.getPersonAverageMarkBySubject(personId, subjectId, period.dateStart, period.dateFinish)

    override suspend fun getPersonMarksBySubject(
        personId: Long,
        subjectId: Long,
        period: Period
    ): List<Mark> =
        markDao.getPersonMarkBySubject(personId, subjectId, period.dateStart, period.dateFinish)
            .map(MarkEntity::asExternalModel)

    override suspend fun getPersonAverageMarkValue(
        personId: Long,
        dateStart: LocalDateTime,
        dateFinish: LocalDateTime
    ): Float = markDao.getPersonAverageMark(personId, dateStart, dateFinish)
}