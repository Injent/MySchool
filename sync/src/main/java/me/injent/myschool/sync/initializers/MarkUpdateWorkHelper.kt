package me.injent.myschool.sync.initializers

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.Constraints
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import me.injent.myschool.sync.R

private val MarkUpdateNotificationId: Int
    get() = "MarkUpdate".hashCode()
private const val MarkUpdateNotificationChannelId = "mark_update_channel"

internal val MarkUpdateWorkConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

internal fun Context.markUpdateForegroundInfo(): ForegroundInfo {
    val notification = NotificationCompat.Builder(
        this,
        SilentChannelId,
    )
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(getString(R.string.mark_update_foreground_title))
        .setPriority(NotificationCompat.PRIORITY_MIN)
        .build()

    return ForegroundInfo(
        SilentNotificationId,
        notification,
    )
}

internal fun Context.showMarkUpdateNotification(content: String, markId: Long) {
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        throw IllegalStateException()
    } else {
        NotificationManagerCompat.from(this)
            .notify(MarkUpdateNotificationId, markUpdateNotification(content, markId))
    }
}

private fun Context.markUpdateNotification(content: String, markId: Long): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            MarkUpdateNotificationChannelId,
            getString(R.string.mark_update_notification_name),
            NotificationManager.IMPORTANCE_HIGH,
        )

        notificationManager.createNotificationChannel(channel)
    }

    val markDetailsIntent = Intent(
        Intent.ACTION_VIEW,
        "${getString(R.string.mark_update_notification_deeplink)}=$markId".toUri(),
        this,
        Class.forName("$packageName.MainActivity")
    )

    val pending: PendingIntent = TaskStackBuilder.create(this).run {
        addNextIntentWithParentStack(markDetailsIntent)
        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
    }

    return NotificationCompat.Builder(
        this,
        MarkUpdateNotificationChannelId,
    )
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(getString(R.string.mark_update_notification_title))
        .setContentText(content)
        .setContentIntent(pending)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()
}