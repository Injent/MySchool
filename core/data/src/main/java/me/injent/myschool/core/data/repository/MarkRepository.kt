package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.injent.myschool.core.data.model.asEntity
import me.injent.myschool.core.database.dao.MarkDao
import me.injent.myschool.core.database.dao.SubjectDao
import me.injent.myschool.core.database.model.MarkEntity
import me.injent.myschool.core.database.model.SubjectEntity
import me.injent.myschool.core.database.model.asExternalModel
import me.injent.myschool.core.model.Mark
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import javax.inject.Inject

interface MarkRepository : Syncable {
    suspend fun getPersonFinalMarkBySubject(personId: Long, subjectId: Long): Float
    fun getPersonMarksBySubject(personId: Long, subjectId: Long): Flow<List<Mark>>
}

class OfflineFirstMarkRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val subjectDao: SubjectDao,
    private val markDao: MarkDao,
    private val userDataRepository: UserDataRepository
) : MarkRepository {
    override suspend fun getPersonFinalMarkBySubject(personId: Long, subjectId: Long): Float {
        return markDao.getPersonMarkValuesBySubject(personId, subjectId)
            .mapNotNull(String::toFloatOrNull)
            .average()
            .toString().take(4).toFloat()
    }

    override fun getPersonMarksBySubject(personId: Long, subjectId: Long): Flow<List<Mark>> {
        return markDao.getPersonMarkBySubject(personId, subjectId)
            .map { it.map(MarkEntity::asExternalModel) }
    }

    override suspend fun synchronize(): Boolean = try {
        val eduGroupId = userDataRepository.getUserContext()!!.eduGroup.id
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val period = networkDataSource.getReportingPeriods(eduGroupId).find {
            it.start <= currentTime && it.finish >= currentTime
        }!!
        val subjectIds = subjectDao.getSubjects().first().map(SubjectEntity::id)
        for (subjectId in subjectIds) {
            val marks = networkDataSource.getEduGroupMarksBySubject(
                eduGroupId,
                subjectId,
                period.start,
                period.finish
            )
            markDao.saveMarks(marks.map { it.asEntity(subjectId) })
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}