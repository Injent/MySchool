package me.injent.myschool.core.network

import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.network.model.*

/**
 * Interface representing network calls to the Dnevnik backend
 */
interface DnevnikNetworkDataSource {
    suspend fun getUserFeed(
        groupId: Long,
        personId: Long,
    ): NetworkUserFeed

    suspend fun getAvatarUrl(userId: Long): String?
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

    suspend fun getHomeworkData(homeworkId: Long): NetworkHomeworkData

    suspend fun getMarkDetails(
        personId: Long,
        periodId: Long,
        markId: Long
    ): NetworkMarkDetails

    suspend fun getReportingPeriod(groupId: Long): List<NetworkPeriod>

    suspend fun getPersonSchedule(
        personId: Long,
        schoolId: Long,
        groupId: Long,
        startDate: Long,
        finishDate: Long
    ): NetworkSchedule

    suspend fun getChatContacts(): ChatContactsResponse
}