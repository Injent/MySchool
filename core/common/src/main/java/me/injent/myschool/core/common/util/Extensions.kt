package me.injent.myschool.core.common.util

import android.content.Context
import android.text.format.Formatter
import kotlinx.datetime.*

fun LocalDateTime.Companion.currentLocalDateTime() =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.Companion.currentDateTimeAtStartOfDay() =
    currentLocalDateTime().date.atTime(0, 0)

fun LocalDateTime.Companion.currentDateTimeAtEndOfDay() =
    currentLocalDateTime().date.atTime(23, 59, 59)

fun Long.toFormattedFileSize(context: Context): String {
    return Formatter.formatFileSize(context, this)
}