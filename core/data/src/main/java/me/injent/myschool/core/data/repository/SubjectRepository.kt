package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.injent.myschool.core.data.model.asEntity
import me.injent.myschool.core.database.dao.SubjectDao
import me.injent.myschool.core.database.model.SubjectEntity
import me.injent.myschool.core.database.model.asExternalModel
import me.injent.myschool.core.model.Subject
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.NetworkSubject
import javax.inject.Inject

interface SubjectRepository : Syncable {
    val subjects: Flow<List<Subject>>
}

class OfflineFirstSubjectRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val subjectDao: SubjectDao,
    private val userDataRepository: UserDataRepository
) : SubjectRepository {
    override val subjects: Flow<List<Subject>>
        get() = subjectDao.getSubjects().map { it.map(SubjectEntity::asExternalModel) }

    override suspend fun synchronize(): Boolean = try {
        val eduGroupId = userDataRepository.getUserContext()!!.eduGroup.id
        val subjects = networkDataSource.getSubjects(eduGroupId).map(NetworkSubject::asEntity)
        subjectDao.saveSubjects(subjects)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}