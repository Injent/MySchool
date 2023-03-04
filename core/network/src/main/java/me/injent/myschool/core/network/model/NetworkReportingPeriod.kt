package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.ReportingPeriod
import me.injent.myschool.core.network.IdSerializer

@Serializable
data class NetworkReportingPeriod(
    @SerialName("id_str")
    @Serializable(IdSerializer::class)
    val id: Long,
    val number: Int,
    val name: String,
    val start: LocalDateTime,
    val finish: LocalDateTime
)

fun NetworkReportingPeriod.asExternalModel() = ReportingPeriod(
    id = id,
    name = name,
    number = number,
    start = start,
    finish = finish
)