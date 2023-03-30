package me.injent.myschool.core.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.injent.myschool.core.network.ApiProvider
import me.injent.myschool.core.network.BuildConfig
import me.injent.myschool.core.network.model.auth.EsiaCredentials
import me.injent.myschool.core.network.model.auth.EsiaLoginResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

/**
 * Retrofit Service used for auth
 */
private interface RetrofitApiProviderApi {
    @POST("/v2/authorizations/bycredentials")
    suspend fun authByCredentials(@Body credentials: EsiaCredentials): EsiaLoginResponse
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

    override suspend fun authByCredentials(credentials: EsiaCredentials): EsiaLoginResponse =
        api.authByCredentials(credentials)
}