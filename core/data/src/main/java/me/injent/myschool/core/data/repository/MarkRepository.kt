package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.data.model.asEntity
import me.injent.myschool.core.database.dao.MarkDao
import me.injent.myschool.core.database.dao.SubjectDao
import me.injent.myschool.core.database.model.SubjectEntity
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import javax.inject.Inject

interface MarkRepository : Synchronizable {
}

class OfflineFirstMarkRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val subjectDao: SubjectDao,
    private val markDao: MarkDao,
    private val userDataRepository: UserDataRepository
) : MarkRepository {
    override suspend fun synchronize(): Boolean = try {
        val from = LocalDateTime.parse("2023-01-11T00:00:00")
        val to = LocalDateTime.parse("2023-05-31T00:00:00")
        val eduGroupId = userDataRepository.getUserContext()!!.eduGroup.id
        val subjectIds = subjectDao.getSubjects().first().map(SubjectEntity::id)
        for (subjectId in subjectIds) {
            val marks = networkDataSource.getEduGroupMarksBySubject(eduGroupId, subjectId, from, to)
            markDao.saveMarks(marks.map { it.asEntity(subjectId) })
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}