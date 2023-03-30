package me.injent.myschool.core.network

import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.network.model.*

/**
 * Interface representing network calls to the Dnevnik backend
 */
interface DnevnikNetworkDataSource {
    suspend fun getMyUserId(): UserIdResponse

    suspend fun getUserFeed(
        date: LocalDateTime,
        personId: Long,
        limit: Int
    ): NetworkUserFeed

    suspend fun getContextPerson(userId: Long): ContextPersonResponse
    suspend fun getClassmates(): List<Long>
    suspend fun getPerson(userId: Long): NetworkPerson?
    suspend fun getPersonsInEduGroup(eduGroupId: Long): List<NetworkShortUserInfo>
    suspend fun getSubjects(eduGroupId: Long): List<NetworkSubject>
    suspend fun getPersonMarksBySubjectAndPeriod(
        personId: Long,
        subjectId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<NetworkMark>

    suspend fun getAverageMark(personId: Long, periodId: Long): Float
    suspend fun getEduGroupMarksBySubject(
        eduGroupId: Long,
        subjectId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<NetworkMark>

    suspend fun getHomeworks(
        schoolId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): NetworkHomeworkData

    suspend fun getRecentMarks(
        personId: Long,
        groupId: Long,
        fromDate: LocalDateTime? = null,
        subjectId: Long? = null,
        limit: Int = 10
    ): RecentMarksResponse

    suspend fun getPersonMarksByPeriod(
        personId: Long,
        schoolId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<NetworkMark>

    suspend fun getLesson(lessonId: Long): NetworkLesson

    suspend fun getPersonGroups(personId: Long): List<NetworkGroup>

    suspend fun getMarkDetails(
        personId: Long,
        periodId: Long,
        markId: Long
    ): MarkDetailsResponse

    suspend fun getReportingPeriod(groupId: Long): List<NetworkPeriod>
}