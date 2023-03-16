package me.injent.myschool.core.common.util

import kotlinx.datetime.*
import java.time.format.DateTimeFormatter

const val DEFAULT_DATE_FORMAT = "dd.MM.yyyy"
const val BIRTHDAY_DATE_FORMAT = "dd MMM yyyy"
const val DEFAULT_DATE_TIME_FORMAT = "dd MMM, HH:mm"

fun LocalDate?.format(pattern: String): String? {
    if (this == null) return null
    val df = DateTimeFormatter.ofPattern(pattern)
    return df.format(this.toJavaLocalDate())
}

fun LocalDateTime.format(pattern: String): String {
    val df = DateTimeFormatter.ofPattern(pattern)
    return df.format(this.toJavaLocalDateTime())
}