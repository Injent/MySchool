package me.injent.myschool.core.common.util

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.text.format.Formatter
import androidx.core.net.toUri
import kotlinx.datetime.*
import kotlin.time.Duration

fun LocalDate.Companion.currentLocalDate() =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDateTime.Companion.currentLocalDateTime() =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.Companion.currentDateTimeAtStartOfDay() =
    currentLocalDateTime().date.atTime(0, 0)

fun LocalDateTime.Companion.currentDateTimeAtEndOfDay() =
    currentLocalDateTime().date.atTime(23, 59, 59)

fun Long.toFormattedFileSize(context: Context): String {
    return Formatter.formatFileSize(context, this)
}

fun LocalDateTime.plus(duration: Duration): LocalDateTime {
    return this.toInstant(TimeZone.currentSystemDefault()).plus(duration)
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

val LocalDateTime.epochSeconds: Long
    get() = this.toInstant(TimeZone.currentSystemDefault()).epochSeconds

fun LocalDateTime.atTimeZone(zone: TimeZone): LocalDateTime {
    return this
        .toJavaLocalDateTime()
        .atZone(TimeZone.currentSystemDefault().toJavaZoneId())
        .withZoneSameInstant(zone.toJavaZoneId())
        .toLocalDateTime()
        .toKotlinLocalDateTime()
}

fun Instant.atTimeZone(zone: TimeZone): Instant {
    return this.toJavaInstant()
        .atZone(zone.toJavaZoneId())
        .toInstant()
        .toKotlinInstant()
}

fun Context.openUrl(url: String) {
    val intent = Intent(ACTION_VIEW, url.toUri())
    startActivity(intent)
}

fun String.unescapeUnicode(): String {
    val sb = StringBuilder()
    var oldIndex = 0
    var i = 0
    while (i + 2 < length) {
        if (substring(i, i + 2) == "\\u") {
            sb.append(substring(oldIndex, i))
            val codePoint = substring(i + 2, i + 6).toInt(16)
            sb.append(Character.toChars(codePoint))
            i += 5
            oldIndex = i + 1
        }
        i++
    }
    sb.append(substring(oldIndex, length))
    return sb.toString()
}