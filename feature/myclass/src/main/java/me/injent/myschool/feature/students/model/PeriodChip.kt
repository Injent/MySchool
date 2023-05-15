package me.injent.myschool.feature.students.model

import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.model.PeriodType

data class PeriodChip(
    val type: PeriodType,
    val number: Int,
    val isCurrent: Boolean,
    val dateStart: LocalDateTime,
    val dateFinish: LocalDateTime
)