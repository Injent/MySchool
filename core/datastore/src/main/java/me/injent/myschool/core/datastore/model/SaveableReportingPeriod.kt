package me.injent.myschool.core.datastore.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.ReportingPeriod

@Serializable
data class SaveableReportingPeriod(
    val id: Long,
    val number: Int,
    val name: String,
    val start: LocalDateTime,
    val finish: LocalDateTime
)

fun SaveableReportingPeriod.asExternalModel() = ReportingPeriod(
    id = id,
    number = number,
    name = name,
    start = start,
    finish = finish
)