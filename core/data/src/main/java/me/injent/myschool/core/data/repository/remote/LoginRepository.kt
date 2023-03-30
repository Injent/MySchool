package me.injent.myschool.core.data.repository.remote

import me.injent.myschool.core.network.ApiProvider
import me.injent.myschool.core.network.BuildConfig
import me.injent.myschool.core.network.model.auth.EsiaCredentials
import me.injent.myschool.core.network.model.auth.EsiaLoginResponse
import javax.inject.Inject

interface LoginRepository {
    suspend fun authByCredentials(login: String, password: String): EsiaLoginResponse
}

private const val SCOPE = "CommonInfo,FriendsAndRelatives,EducationalInfo,SocialInfo,Wall,Messages,Files,PersonalData"

class LoginRepositoryImpl @Inject constructor(
    private val apiProvider: ApiProvider
) : LoginRepository {
    override suspend fun authByCredentials(login: String, password: String): EsiaLoginResponse =
        apiProvider.authByCredentials(
            EsiaCredentials(
                userName = login,
                password = password,
                scope = SCOPE,
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET
            )
        )
}