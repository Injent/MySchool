package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import me.injent.myschool.core.common.util.DIGITS_NUMBER_IN_MARK
import me.injent.myschool.core.data.model.asEntity
import me.injent.myschool.core.data.util.RepoDependency
import me.injent.myschool.core.database.dao.MarkDao
import me.injent.myschool.core.database.dao.SubjectDao
import me.injent.myschool.core.database.model.MarkEntity
import me.injent.myschool.core.database.model.SubjectEntity
import me.injent.myschool.core.database.model.asExternalModel
import me.injent.myschool.core.model.Mark
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import javax.inject.Inject

@RepoDependency(UserDataRepository::class, SubjectRepository::class)
interface MarkRepository : Syncable {
    suspend fun getPersonFinalMarkBySubject(personId: Long, subjectId: Long): Float
    fun getPersonMarksBySubject(personId: Long, subjectId: Long): Flow<List<Mark>>
    fun getPersonAverageMark(personId: Long): Flow<Float>
}

class OfflineFirstMarkRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val subjectDao: SubjectDao,
    private val markDao: MarkDao,
    private val userDataRepository: UserDataRepository
) : MarkRepository {

    override suspend fun synchronize(): Boolean = try {
        val eduGroupId = userDataRepository.getUserContext()!!.eduGroup.id
        val period = userDataRepository.userData.first().period!!
        markDao.deleteDeprecatedMarks(period.start)

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
    override suspend fun getPersonFinalMarkBySubject(personId: Long, subjectId: Long): Float {
        return markDao.getPersonMarkValuesBySubject(personId, subjectId)
            .ifEmpty { return 0f }
            .mapNotNull(String::toFloatOrNull)
            .average()
            .toString().take(DIGITS_NUMBER_IN_MARK).toFloat()
    }

    override fun getPersonMarksBySubject(personId: Long, subjectId: Long): Flow<List<Mark>> {
        return markDao.getPersonMarkBySubject(personId, subjectId)
            .map { it.map(MarkEntity::asExternalModel) }
    }

    override fun getPersonAverageMark(personId: Long): Flow<Float> {
        return markDao.getPersonAverageMark(personId)
            .map {
                it.mapNotNull(String::toFloatOrNull)
                    .average()
                    .toString().take(4).toFloat()
            }
    }
}