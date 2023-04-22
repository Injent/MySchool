package me.injent.myschool.core.data.repository

import me.injent.myschool.core.model.Period
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.NetworkPeriod
import me.injent.myschool.core.network.model.asExternalModel
import javax.inject.Inject

interface ReportingPeriodRepository {
    suspend fun getReportingPeriods(groupId: Long): List<Period>
}

class RemoteReportingPeriodRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource
) : ReportingPeriodRepository {
    override suspend fun getReportingPeriods(groupId: Long): List<Period> =
        networkDataSource.getReportingPeriod(groupId).map(NetworkPeriod::asExternalModel)
}