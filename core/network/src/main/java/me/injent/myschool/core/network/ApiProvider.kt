package me.injent.myschool.core.network

import me.injent.myschool.core.network.model.NetworkUserContext
import me.injent.myschool.core.network.model.UserIdResponse

interface ApiProvider {
    suspend fun auth(accessToken: String, userId: Long): NetworkUserContext
    suspend fun userId(accessToken: String): UserIdResponse
}