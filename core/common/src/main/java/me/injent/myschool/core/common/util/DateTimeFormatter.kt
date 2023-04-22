package me.injent.myschool.core.common.util

import android.icu.text.RelativeDateTimeFormatter
import android.os.Build
import kotlinx.datetime.*
import java.time.format.DateTimeFormatter


const val DEFAULT_DATE_FORMAT = "dd.MM.yyyy"
const val BIRTHDAY_DATE_FORMAT = "dd MMM yyyy"
const val MONTH_DATE_FORMAT = "dd MMM"
const val DEFAULT_DATE_TIME_FORMAT = "dd MMM, HH:mm"

fun LocalDate.format(pattern: String): String {
    val df = DateTimeFormatter.ofPattern(pattern)
    return df.format(this.toJavaLocalDate())
}

fun LocalDateTime.relativeTimeFormat(dateTime: LocalDateTime): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val formatter = RelativeDateTimeFormatter.getInstance()

        val startInstant = this.toInstant(TimeZone.UTC)
        val endInstant = dateTime.toInstant(TimeZone.currentSystemDefault())

        val difference = endInstant - startInstant
        val diffInDays = difference.inWholeDays
        val diffInHours = difference.inWholeHours
        val diffInMinutes = difference.inWholeMinutes

        val relativeTimeUnit: RelativeDateTimeFormatter.RelativeUnit

        val quantity = if (diffInDays < 1) {
            if (diffInHours < 1) {
                if (diffInMinutes < 1) {
                    relativeTimeUnit = RelativeDateTimeFormatter.RelativeUnit.SECONDS
                    difference.inWholeSeconds
                } else {
                    relativeTimeUnit = RelativeDateTimeFormatter.RelativeUnit.MINUTES
                    diffInMinutes
                }
            } else {
                relativeTimeUnit = RelativeDateTimeFormatter.RelativeUnit.HOURS
                diffInHours
            }
        } else {
            relativeTimeUnit = RelativeDateTimeFormatter.RelativeUnit.DAYS
            diffInDays
        }.toDouble()

        formatter.format(
            quantity,
            RelativeDateTimeFormatter.Direction.LAST,
            relativeTimeUnit
        )
    } else {
        "SDK < 24 error"
    }
}

fun LocalDateTime.format(pattern: String): String {
    val df = DateTimeFormatter.ofPattern(pattern)
    return df.format(this.toJavaLocalDateTime())
}