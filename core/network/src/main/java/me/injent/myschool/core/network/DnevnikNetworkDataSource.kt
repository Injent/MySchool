package me.injent.myschool.core.network

import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.common.result.Result
import me.injent.myschool.core.model.Mark
import me.injent.myschool.core.network.model.*

/**
 * Interface representing network calls to the Dnevnik backend
 */
interface DnevnikNetworkDataSource {
    suspend fun getUserContext(): NetworkUserContext
    suspend fun getClassmates(): List<Long>
    suspend fun getPerson(userId: Long): NetworkPerson
    suspend fun getPersonsInEduGroup(eduGroupId: Long): List<NetworkShortUserInfo>
    suspend fun getReportingPeriods(eduGroupId: Long): List<NetworkReportingPeriod>
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
}

