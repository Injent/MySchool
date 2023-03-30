package me.injent.myschool.core.data.repository

import me.injent.myschool.core.model.Group
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.NetworkGroup
import me.injent.myschool.core.network.model.asExternalModel
import javax.inject.Inject

interface GroupRepository {
    suspend fun getPersonGroups(personId: Long): List<Group>
}

class GroupRepositoryImpl @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource
) : GroupRepository {
    override suspend fun getPersonGroups(personId: Long): List<Group> =
        networkDataSource.getPersonGroups(personId).map(NetworkGroup::asExternalModel)
}