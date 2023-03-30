package me.injent.myschool.core.network

import me.injent.myschool.core.network.model.auth.EsiaCredentials
import me.injent.myschool.core.network.model.auth.EsiaLoginResponse

interface ApiProvider {
    suspend fun authByCredentials(credentials: EsiaCredentials): EsiaLoginResponse
}