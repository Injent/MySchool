package me.injent.myschool.core.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.injent.myschool.core.network.DnevnikApi
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for Dnevnik API
 */
private interface RetrofitDnevnikApi {
    @GET("/mobile/v7.0/users/{userId}/context")
    suspend fun getUserContext(@Path("userId") userId: Long): ContextPersonResponse

    @GET("/mobile/v7.0/persons/{personId}/groups/{groupId}/periods/{periodId}/periodMarks")
    suspend fun getPersonMarks(
        @Path("personId") personId: Long,
        @Path("groupId") groupId: Long,
        @Path("periodId") periodId: Long
    ): PersonMarksResponse

    @GET("/v2/users/me/classmates")
    suspend fun getClassmates(): List<Long>

    @GET("/v2/users/me")
    suspend fun getMyUserId(): UserIdResponse

    @GET("/v2/users/{userId}")
    suspend fun getPerson(
        @Path("userId") userId: Long
    ): NetworkPerson

    @GET(DnevnikApi.PEOPLE_IN_EDUGROUP)
    suspend fun getPeopleInEduGroup(
        @Path(DnevnikApi.EDUGROUP_ID) eduGroupId: Long
    ): List<NetworkShortUserInfo>

    @GET(DnevnikApi.SUBJECTS)
    suspend fun getSubjects(@Path(DnevnikApi.EDUGROUP_ID) eduGroupId: Long): List<NetworkSubject>

    @GET(DnevnikApi.PERSON_MARK_BY_PERIOD_AND_SUBJECT)
    suspend fun getPersonMarksBySubjectAndPeriod(
        @Path(DnevnikApi.PERSON_ID) personId: Long,
        @Path(DnevnikApi.SUBJECT_ID) subjectId: Long,
        @Path(DnevnikApi.FROM_PERIOD) from: LocalDateTime,
        @Path(DnevnikApi.TO_PERIOD) to: LocalDateTime
    ): List<NetworkMark>

    @GET(DnevnikApi.AVERAGE_MARK)
    suspend fun getAverageMark(
        @Path(DnevnikApi.PERSON_ID) personId: Long,
        @Path(DnevnikApi.PERIOD_ID) periodId: Long
    ): String

    @GET(DnevnikApi.EDUGROUP_MARKS)
    suspend fun getEduGroupMarksBySubject(
        @Path(DnevnikApi.EDUGROUP_ID) eduGroupId: Long,
        @Path(DnevnikApi.SUBJECT_ID) subjectId: Long,
        @Path(DnevnikApi.FROM_PERIOD) from: LocalDateTime,
        @Path(DnevnikApi.TO_PERIOD) to: LocalDateTime
    ): List<NetworkMark>

    @GET(DnevnikApi.HOMEWORKS)
    suspend fun getHomeworks(
        @Path(DnevnikApi.SCHOOL_ID) schoolId: Long,
        @Query("startDate") from: LocalDateTime,
        @Query("endDate") to: LocalDateTime
    ): NetworkHomeworkData

    @GET(DnevnikApi.PERSON_MARKS_BY_PERIOD)
    suspend fun getPersonMarksByPeriod(
        @Path(DnevnikApi.PERSON_ID) personId: Long,
        @Path(DnevnikApi.SCHOOL_ID) schoolId: Long,
        @Path(DnevnikApi.FROM_PERIOD) from: LocalDateTime,
        @Path(DnevnikApi.TO_PERIOD) to: LocalDateTime
    ): List<NetworkMark>

    @GET("/v2/lessons/{lessonId}")
    suspend fun getLesson(@Path("lessonId") lessonId: Long): NetworkLesson
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

    override suspend fun getMyUserId(): UserIdResponse =
        api.getMyUserId()

    override suspend fun getContextPerson(userId: Long): ContextPersonResponse =
        api.getUserContext(userId)

    override suspend fun getClassmates(): List<Long> =
        api.getClassmates()

    override suspend fun getPerson(userId: Long): NetworkPerson =
        api.getPerson(userId)

    override suspend fun getPersonsInEduGroup(eduGroupId: Long): List<NetworkShortUserInfo> =
        api.getPeopleInEduGroup(eduGroupId)

    override suspend fun getSubjects(eduGroupId: Long): List<NetworkSubject> =
        api.getSubjects(eduGroupId)

    override suspend fun getPersonMarksBySubjectAndPeriod(
        personId: Long,
        subjectId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<NetworkMark> =
        api.getPersonMarksBySubjectAndPeriod(personId, subjectId, from, to)

    override suspend fun getAverageMark(personId: Long, periodId: Long): Float =
        api.getAverageMark(personId, periodId).replace(',', '.').toFloat()

    override suspend fun getEduGroupMarksBySubject(
        eduGroupId: Long,
        subjectId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<NetworkMark> =
        api.getEduGroupMarksBySubject(
            eduGroupId,
            subjectId,
            from,
            to
        )

    override suspend fun getHomeworks(
        schoolId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): NetworkHomeworkData =
        api.getHomeworks(
            schoolId = schoolId,
            from = from,
            to = to
        )

    override suspend fun getRecentMarks(
        personId: Long,
        eduGroupId: Long,
        fromDate: LocalDateTime?,
        toDate: LocalDateTime?,
        limit: Int?
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getPersonMarksByPeriod(
        personId: Long,
        schoolId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<NetworkMark> =
        api.getPersonMarksByPeriod(
            personId,
            schoolId,
            from,
            to
        )

    override suspend fun getLesson(lessonId: Long): NetworkLesson =
        api.getLesson(lessonId)
}