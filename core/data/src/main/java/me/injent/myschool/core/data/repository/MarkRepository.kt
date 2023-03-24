package me.injent.myschool.core.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.data.model.asEntity
import me.injent.myschool.core.data.util.RepoDependency
import me.injent.myschool.core.database.dao.MarkDao
import me.injent.myschool.core.database.dao.PersonDao
import me.injent.myschool.core.database.dao.SubjectDao
import me.injent.myschool.core.database.model.MarkEntity
import me.injent.myschool.core.database.model.SubjectEntity
import me.injent.myschool.core.database.model.asExternalModel
import me.injent.myschool.core.model.Mark
import me.injent.myschool.core.model.MarkDetails
import me.injent.myschool.core.model.Period
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.asExternalModel
import javax.inject.Inject

@RepoDependency(UserContextRepository::class, SubjectRepository::class)
interface MarkRepository : Syncable {
    suspend fun getPersonFinalMarkBySubject(personId: Long, subjectId: Long): Float
    fun getPersonMarksBySubjectStream(personId: Long, subjectId: Long): Flow<List<Mark>>
    suspend fun getPersonAverageMarkValue(personId: Long): Float
    suspend fun receiveNewMarks(): ReceivedMarksResult
    suspend fun receiveClassmatesMarks(): Boolean
    fun getMark(markId: Long): Flow<Mark>
    suspend fun getMarkDetails(
        personId: Long,
        groupId: Long,
        markId: Long
    ): MarkDetails
}

sealed interface ReceivedMarksResult {
    object NotChanged : ReceivedMarksResult
    data class NewMark(
        val value: String,
        val subjectName: String
    ) : ReceivedMarksResult
    data class MultipleMarks(val marksCount: Int) : ReceivedMarksResult
    object Error : ReceivedMarksResult
}

class OfflineFirstMarkRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val subjectDao: SubjectDao,
    private val markDao: MarkDao,
    private val userContextRepository: UserContextRepository,
    private val userDataRepository: UserDataRepository,
    private val personDao: PersonDao
) : MarkRepository {

    override suspend fun synchronize(): Boolean = try {
        val groupId: Long
        val period: Period
        userContextRepository.userContext.first()!!.run {
            groupId = group.id
            period = reportingPeriodGroup.periods.find(Period::isCurrent)!!
        }

        markDao.deleteDeprecatedMarks(period.dateStart)

        val subjectIds = subjectDao.getSubjects().first().map(SubjectEntity::id)
        for (subjectId in subjectIds) {
            val marks = networkDataSource.getEduGroupMarksBySubject(
                groupId,
                subjectId,
                period.dateStart,
                period.dateFinish
            )
            markDao.saveMarks(marks.map { it.asEntity(subjectId) })
        }
        true
    } catch (e: Exception) {
        Log.e("MarkRepository", "Failed to sync")
        e.printStackTrace()
        false
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
                    dateStart = this.dateStart
                    dateEnd = userDataRepository.userData.first().lastMarksSyncDateTime
                        ?: this.dateFinish
                }
            }

            val marks = networkDataSource.getPersonMarksByPeriod(
                personId = personId,
                schoolId = schoolId,
                from = dateStart,
                to = dateEnd
            )
                .filterNot { markDao.contains(it.id) }

            if (marks.isNotEmpty()) {
                val marksEntities = mutableListOf<MarkEntity>()
                for (mark in marks) {
                    val subject = networkDataSource.getLesson(mark.lessonId).subject!!
                    marksEntities.add(mark.asEntity(subject.id))

                    if (marks.size == 1) {
                        markDao.saveMark(marksEntities.first())
                        return ReceivedMarksResult.NewMark(mark.value, subject.name)
                    }
                }
                markDao.saveMarks(marksEntities)
                return ReceivedMarksResult.MultipleMarks(marks.size)
            }

            userDataRepository.updateMarksSyncDateTime()
            return ReceivedMarksResult.NotChanged
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
                from = period.dateStart,
                to = period.dateFinish
            ).filterNot { markDao.contains(it.id) }

            marks.forEach { mark ->
                val subjectId = networkDataSource.getLesson(mark.lessonId).subject!!.id
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

    override suspend fun getMarkDetails(personId: Long, groupId: Long, markId: Long): MarkDetails =
        networkDataSource.getMarkDetails(personId, groupId, markId).asExternalModel()

    override suspend fun getPersonFinalMarkBySubject(personId: Long, subjectId: Long): Float =
        markDao.getPersonAverageMarkBySubject(personId, subjectId)

    override fun getPersonMarksBySubjectStream(personId: Long, subjectId: Long): Flow<List<Mark>> {
        return markDao.getPersonMarkBySubject(personId, subjectId)
            .map { it.map(MarkEntity::asExternalModel) }
    }

    override suspend fun getPersonAverageMarkValue(personId: Long): Float =
        markDao.getPersonAverageMark(personId)
}