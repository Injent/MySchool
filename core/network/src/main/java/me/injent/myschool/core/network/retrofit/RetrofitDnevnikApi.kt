package me.injent.myschool.core.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.injent.myschool.core.network.BuildConfig
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

    @GET("/mobile/v7.0/persons/{personId}/groups/{groupId}/periods/{periodId}/periodMarks")
    suspend fun getPersonMarks(
        @Path("personId") personId: Long,
        @Path("groupId") groupId: Long,
        @Path("periodId") periodId: Long
    ): PersonMarksResponse

    @GET("/v2/users/me/classmates")
    suspend fun getClassmates(): List<Long>

    @GET("/mobile/v4/persons/{personId}/groups/{groupId}/important")
    suspend fun getUserFeed(
        @Path("groupId") groupId: Long,
        @Path("personId") personId: Long
    ): NetworkUserFeed

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

    @GET("/v2/persons/{person}/schools/{school}/marks/{from}/{to}")
    suspend fun getPersonMarksByPeriod(
        @Path("person") personId: Long,
        @Path("school") schoolId: Long,
        @Path("from") from: LocalDateTime,
        @Path("to") to: LocalDateTime
    ): List<NetworkMark>

    @GET("/v2/lessons/{lesson}")
    suspend fun getLesson(@Path("lesson") lessonId: Long): NetworkLesson

    @GET("/mobile/v7.0/persons/{personId}/groups/{groupId}/marks/{markId}/markDetails")
    suspend fun getMarkDetails(
        @Path("personId") personId: Long,
        @Path("groupId") groupId: Long,
        @Path("markId") markId: Long
    ): NetworkMarkDetails

    @GET("/v2/persons/{personId}/edu-groups")
    suspend fun getPersonGroups(@Path("personId") personId: Long): List<NetworkGroup>

    @GET("/v2/edu-groups/{groupId}/reporting-periods")
    suspend fun getReportingPeriodGroup(@Path("groupId") groupId: Long): List<NetworkPeriod>

    @GET("/v2/persons/{personId}/group/{groupId}/recentmarks")
    suspend fun getRecentMarks(
        @Path("personId") personId: Long,
        @Path("groupId") groupId: Long,
        @Query("fromDate") fromDate: LocalDateTime? = null,
        @Query("subject") subjectId: Long? = null,
        @Query("limit") limit: Int = 10
    ): RecentMarksResponse

    @GET("/mobile/v3/persons/{personId}/schools/{schoolId}/groups/{groupId}/diary")
    suspend fun getPersonSchedule(
        @Path("personId") personId: Long,
        @Path("schoolId") schoolId: Long,
        @Path("groupId") groupId: Long,
        @Query("startDate") startDate: Long,
        @Query("finishDate") finishDate: Long
    ): NetworkSchedule

    @GET("/v2/users/me/school/homeworks")
    suspend fun getHomeworkData(@Query("homeworkId") homeworkId: Long): NetworkHomeworkData

    @GET("/mobile/v4/users/{userId}/avatar")
    suspend fun getAvatarUrl(@Path("userId") userId: Long): AvatarResponse

    @GET("/mobile/v7.0/chat/closecontacts")
    suspend fun getChatContacts(): ChatContactsResponse
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
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(client)
        .addConverterFactory(
            @OptIn(ExperimentalSerializationApi::class)
            jsonFactory.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitDnevnikApi::class.java)

    override suspend fun getUserFeed(groupId: Long, personId: Long): NetworkUserFeed =
        api.getUserFeed(groupId, personId)

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
    ): List<NetworkMark> = api.getPersonMarksBySubjectAndPeriod(personId, subjectId, from, to)

    override suspend fun getAverageMark(personId: Long, periodId: Long): Float =
        api.getAverageMark(personId, periodId).replace(',', '.').toFloat()

    override suspend fun getEduGroupMarksBySubject(
        eduGroupId: Long,
        subjectId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<NetworkMark> = api.getEduGroupMarksBySubject(eduGroupId, subjectId, from, to)

    override suspend fun getHomeworks(
        schoolId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): NetworkHomeworkData = api.getHomeworks(schoolId, from, to)

    override suspend fun getRecentMarks(
        personId: Long,
        groupId: Long,
        fromDate: LocalDateTime?,
        subjectId: Long?,
        limit: Int
    ) = api.getRecentMarks(personId, groupId, fromDate, subjectId, limit)

    override suspend fun getPersonMarksByPeriod(
        personId: Long,
        schoolId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<NetworkMark> = api.getPersonMarksByPeriod(personId, schoolId, from, to)

    override suspend fun getLesson(lessonId: Long): NetworkLesson =
        api.getLesson(lessonId)

    override suspend fun getMarkDetails(
        personId: Long,
        periodId: Long,
        markId: Long
    ): NetworkMarkDetails = api.getMarkDetails(personId, periodId, markId)

    override suspend fun getPersonGroups(personId: Long): List<NetworkGroup> {
        return api.getPersonGroups(personId)
    }

    override suspend fun getHomeworkData(homeworkId: Long): NetworkHomeworkData =
        api.getHomeworkData(homeworkId)

    override suspend fun getReportingPeriod(groupId: Long): List<NetworkPeriod> =
        api.getReportingPeriodGroup(groupId)

    override suspend fun getPersonSchedule(
        personId: Long,
        schoolId: Long,
        groupId: Long,
        startDate: Long,
        finishDate: Long
    ): NetworkSchedule = api.getPersonSchedule(
        personId,
        schoolId,
        groupId,
        startDate,
        finishDate
    )

    override suspend fun getAvatarUrl(userId: Long): String? =
        api.getAvatarUrl(userId).avatar.url

    override suspend fun getChatContacts(): ChatContactsResponse =
        api.getChatContacts()
}