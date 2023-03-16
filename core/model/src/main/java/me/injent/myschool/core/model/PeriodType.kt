package me.injent.myschool.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class PeriodType {
    HalfYear,
    Quarter,
    Semester,
    Trimester,
    Module
}