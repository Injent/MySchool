package me.injent.myschool.core.common.util

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.text.format.Formatter
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import kotlinx.datetime.*
import java.io.File
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

fun File.provideUri(context: Context): Uri {
    return if (SDK_INT < 24) {
        Uri.fromFile(this)
    } else {
        FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            this
        )
    }
}