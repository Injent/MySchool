package me.injent.myschool.core.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.injent.myschool.core.network.ApiProvider
import me.injent.myschool.core.network.BuildConfig
import me.injent.myschool.core.network.model.NetworkUserContext
import me.injent.myschool.core.network.model.UserIdResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.*
import javax.inject.Inject

/**
 * Retrofit Service used for auth
 */
private interface RetrofitApiProviderApi {
    @GET("/mobile/v7.0/users/{userId}/context")
    suspend fun auth(
        @Path("userId") userId: Long,
        @Query("access_token") accessToken: String,
    ): NetworkUserContext

    @GET("/v2/users/me")
    suspend fun userId(
        @Query("access_token") accessToken: String,
    ): UserIdResponse
}

class RetrofitApiProvider @Inject constructor(
    jsonFactory: Json,
    client: OkHttpClient
) : ApiProvider {
    private val api = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(client)
        .addConverterFactory(
            @OptIn(ExperimentalSerializationApi::class)
            jsonFactory.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitApiProviderApi::class.java)

    override suspend fun auth(accessToken: String, userId: Long): NetworkUserContext =
        api.auth(userId, accessToken)

    override suspend fun userId(accessToken: String): UserIdResponse =
        api.userId(accessToken)
}