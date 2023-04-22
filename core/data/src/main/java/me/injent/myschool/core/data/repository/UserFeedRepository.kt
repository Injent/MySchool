package me.injent.myschool.core.data.repository

import me.injent.myschool.core.model.UserFeed
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.asExternalModel
import javax.inject.Inject

interface UserFeedRepository {
    suspend fun getUserFeed(
        groupId: Long,
        personId: Long,
    ): UserFeed
}

class RemoteUserFeedRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource
) : UserFeedRepository {
    override suspend fun getUserFeed(groupId: Long, personId: Long): UserFeed =
        networkDataSource.getUserFeed(groupId, personId).asExternalModel()
}