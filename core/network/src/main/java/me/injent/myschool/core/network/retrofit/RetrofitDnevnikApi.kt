package me.injent.myschool.core.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import me.injent.myschool.core.common.result.Result
import me.injent.myschool.core.model.ExternalUserProfile
import me.injent.myschool.core.model.ShortUserInfo
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.network.DnevnikApi
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrapper for data provided from the [RetrofitDnevnikApi]
 */
@Serializable
private data class NetworkResponse<T>(
    val data: T,
)

/**
 * Retrofit API declaration for Dnevnik API
 */
private interface RetrofitDnevnikApi {
    @GET(DnevnikApi.V2.CHECK_TOKEN_EXPIRATION)
    suspend fun checkTokenExpiration(): String?

    @GET(DnevnikApi.V2.CONTEXT)
    suspend fun getUserContext(): Response<NetworkUserContext>

    @GET(DnevnikApi.V2.EXTERNAL_USER_PROFILE)
    suspend fun getExternalUserProfile(
        @Path(DnevnikApi.USER_ID) userId: Long
    ): NetworkExternalUserProfile

    @GET(DnevnikApi.V2.PEOPLE_IN_EDUGROUP)
    suspend fun getPeopleInEduGroup(
        @Path(DnevnikApi.EDUGROUP_ID) eduGroupId: Long
    ): List<NetworkShortUserInfo>

    @GET(DnevnikApi.V2.REPORTING_PERIODS)
    suspend fun getReportingPeriods(
        @Path(DnevnikApi.EDUGROUP_ID) eduGroupId: Long
    ): List<NetworkReportingPeriod>
}

/**
 * [Retrofit] is backend of [DnevnikNetworkDataSource]
 */
@Singleton
class RetrofitDnevnik @Inject constructor(
    jsonFactory: Json,
    client: OkHttpClient
) : DnevnikNetworkDataSource {
    private val api = Retrofit.Builder()
        .baseUrl(DnevnikApi.BASE_URL)
        .client(client)
        .addConverterFactory(
            @OptIn(ExperimentalSerializationApi::class)
            jsonFactory.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitDnevnikApi::class.java)

    override suspend fun isTokenActive(): Boolean
        = !api.checkTokenExpiration().isNullOrEmpty()

    override suspend fun getUserContext(): Result<NetworkUserContext> {
        val response = api.getUserContext()
        return when(response.code()) {
            200 -> Result.Success(response.body()!!)
            else -> {
                val errorMessage = JSONObject(response.errorBody()?.string() ?: "")
                    .getString("description")
                Result.Error(IllegalStateException(errorMessage))
            }
        }
    }

    override suspend fun getExternalUserProfile(userId: Long): NetworkExternalUserProfile
        = api.getExternalUserProfile(userId)

    override suspend fun getPersonsInEduGroup(eduGroupId: Long): List<NetworkShortUserInfo>
        = api.getPeopleInEduGroup(eduGroupId)

    override suspend fun getReportingPeriods(eduGroupId: Long): List<NetworkReportingPeriod>
        = api.getReportingPeriods(eduGroupId)
}