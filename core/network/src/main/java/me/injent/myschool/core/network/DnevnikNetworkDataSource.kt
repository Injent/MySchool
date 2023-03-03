package me.injent.myschool.core.network

import me.injent.myschool.core.network.model.NetworkPerson
import me.injent.myschool.core.network.model.NetworkShortUserInfo
import me.injent.myschool.core.network.model.NetworkUserContext
import me.injent.myschool.core.common.result.Result
import me.injent.myschool.core.network.model.NetworkReportingPeriod

/**
 * Interface representing network calls to the Dnevnik backend
 */
interface DnevnikNetworkDataSource {
    suspend fun getUserContext(): Result<NetworkUserContext>
    suspend fun getClassmates(): List<Long>
    suspend fun getPerson(userId: Long): NetworkPerson
    suspend fun getPersonsInEduGroup(eduGroupId: Long): List<NetworkShortUserInfo>
    suspend fun getReportingPeriods(eduGroupId: Long): List<NetworkReportingPeriod>
}

