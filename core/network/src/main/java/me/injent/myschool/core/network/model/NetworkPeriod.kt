package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.EpochLocalDateTimeSerializer
import me.injent.myschool.core.model.Period
import me.injent.myschool.core.model.PeriodType

@Serializable
data class NetworkPeriod(
    val id: Long,
    val isCurrent: Boolean,
    @Serializable(EpochLocalDateTimeSerializer::class)
    val dateFinish: LocalDateTime,
    @Serializable(EpochLocalDateTimeSerializer::class)
    val dateStart: LocalDateTime,
    val number: Int,
    val studyYear: Int,
    @Serializable
    val type: PeriodType
)

fun NetworkPeriod.asExternalModel() = Period(
    id = id,
    isCurrent = isCurrent,
    dateFinish = dateFinish,
    dateStart = dateStart,
    number = number,
    studyYear = studyYear,
    type = type
)