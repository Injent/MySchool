package me.injent.myschool.core.model

import kotlinx.datetime.Instant

/**
 *
 */
data class ReportingPeriod(
    val number: Int,
    val name: String,
    val id: String,
    val start: Instant,
    val finish: Instant
)