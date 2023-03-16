package me.injent.myschool.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportingPeriodGroup(
    val id: Long,
    val periods: List<Period>
)