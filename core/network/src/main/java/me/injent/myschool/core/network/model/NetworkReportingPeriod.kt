package me.injent.myschool.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.ReportingPeriod

@Serializable
data class NetworkReportingPeriod(
    val number: Int,
    val name: String,
    @SerialName("id_str")
    val id: String,
    @Serializable(with = InstantSerializer::class)
    val start: Instant,
    @Serializable(with = InstantSerializer::class)
    val finish: Instant
)

fun NetworkReportingPeriod.asExternalModel() = ReportingPeriod(
    id = id,
    name = name,
    number = number,
    start = start,
    finish = finish
)