package me.injent.myschool.core.model

import kotlinx.datetime.LocalDate

data class Birthday(
    val personId: Long,
    val personName: String,
    val date: LocalDate,
    val daysUntil: Int
)
