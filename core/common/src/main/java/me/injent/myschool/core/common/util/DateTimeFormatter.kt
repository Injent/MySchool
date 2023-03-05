package me.injent.myschool.core.common.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

const val DEFAULT_DATE_FORMAT = "dd.MM.yyyy"

fun LocalDate?.format(pattern: String): String? {
    if (this == null) return null
    val df = DateTimeFormatter.ofPattern(pattern)
    return df.format(this.toJavaLocalDate())
}