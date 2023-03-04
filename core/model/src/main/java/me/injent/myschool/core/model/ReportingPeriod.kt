package me.injent.myschool.core.model

import kotlinx.datetime.LocalDateTime

/**
 *
 */
data class ReportingPeriod(
    val id: Long,
    val number: Int,
    val name: String,
    val start: LocalDateTime,
    val finish: LocalDateTime
)