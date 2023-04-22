package me.injent.myschool.core.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import me.injent.myschool.core.data.model.asEntity
import me.injent.myschool.core.data.util.RepoDependency
import me.injent.myschool.core.database.dao.SubjectDao
import me.injent.myschool.core.database.model.SubjectEntity
import me.injent.myschool.core.database.model.asExternalModel
import me.injent.myschool.core.model.Subject
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.NetworkSubject
import javax.inject.Inject

interface SubjectRepository : Syncable {
    val subjects: Flow<List<Subject>>
    fun getSubject(subjectId: Long): Flow<Subject>
    suspend fun getSubjectName(subjectId: Long): String
}

@RepoDependency(UserContextRepository::class)
class OfflineFirstSubjectRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val subjectDao: SubjectDao,
    private val userContextRepository: UserContextRepository
) : SubjectRepository {
    override suspend fun synchronize(onProgress: ((Int) -> Unit)?): Boolean = try {
        val groupId = userContextRepository.userContext.first()!!.group.id
        val subjects = networkDataSource.getSubjects(groupId).map(NetworkSubject::asEntity)
        subjectDao.saveSubjects(subjects)
        onProgress?.invoke(100)
        true
    } catch (e: Exception) {
        Log.e("SubjectRepository", "Failed to sync")
        e.printStackTrace()
        false
    }

    override val subjects: Flow<List<Subject>>
        get() = subjectDao.getSubjects().map { it.map(SubjectEntity::asExternalModel) }

    override fun getSubject(subjectId: Long): Flow<Subject> =
        subjectDao.getSubject(subjectId).map(SubjectEntity::asExternalModel)

    override suspend fun getSubjectName(subjectId: Long): String =
        subjectDao.getSubjectName(subjectId)
}