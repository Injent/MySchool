package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.ReportingPeriodGroup

@Serializable
data class NetworkReportingPeriodGroup(
    val id: Long,
    val periods: List<NetworkPeriod>
)

fun NetworkReportingPeriodGroup.asExternalModel() = ReportingPeriodGroup(
    id = id,
    periods = periods.map(NetworkPeriod::asExternalModel)
)