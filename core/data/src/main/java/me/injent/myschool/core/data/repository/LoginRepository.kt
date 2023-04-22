package me.injent.myschool.core.data.repository

import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.network.ApiProvider
import javax.inject.Inject

interface LoginRepository {
    suspend fun login(accessToken: String): UserContext
}

class RemoteLoginRepository @Inject constructor(
    private val apiProvider: ApiProvider
) : LoginRepository {
    override suspend fun login(accessToken: String): UserContext =
        apiProvider.auth(accessToken, apiProvider.userId(accessToken).id).getUserContext()
}