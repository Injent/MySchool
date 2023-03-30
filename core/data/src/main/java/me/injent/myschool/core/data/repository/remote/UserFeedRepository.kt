package me.injent.myschool.core.data.repository.remote

import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.model.UserFeed
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.asExternalModel
import javax.inject.Inject

interface UserFeedRepository {
    suspend fun getUserFeed(
        date: LocalDateTime,
        personId: Long,
        limit: Int
    ): UserFeed
}

class RemoteUserFeedRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource
) : UserFeedRepository {
    override suspend fun getUserFeed(date: LocalDateTime, personId: Long, limit: Int): UserFeed =
        networkDataSource.getUserFeed(date, personId, limit).asExternalModel()
}