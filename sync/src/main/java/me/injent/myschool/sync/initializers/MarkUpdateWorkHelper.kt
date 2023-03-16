package me.injent.myschool.sync.initializers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import me.injent.myschool.sync.R

private const val MarkUpdateNotificationId = 1
private const val MarkUpdateNotificationChannelID = "MarkUpdateNotificationChannel"

// All sync work needs an internet connectionS
val MarkUpdateWorkConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

/**
 * Foreground information for sync on lower API levels when sync workers are being
 * run with a foreground service
 */
fun Context.markUpdateForegroundInfo() = ForegroundInfo(
    MarkUpdateNotificationId,
    markUpdateWorkNotification(""),
)

fun Context.showMarkUpdateNotification(content: String) {
    NotificationManagerCompat.from(this)
        .notify(MarkUpdateNotificationId, markUpdateWorkNotification(content))
}

/**
 * Notification displayed on lower API levels when sync workers are being
 * run with a foreground service
 */
private fun Context.markUpdateWorkNotification(
    content: String
): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            SyncNotificationChannelID,
            getString(R.string.mark_update_notification_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        )

        val notificationManager: NotificationManager? =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        notificationManager?.createNotificationChannel(channel)
    }

    return NotificationCompat.Builder(
        this,
        SyncNotificationChannelID,
    )
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(getString(R.string.mark_update_notification_title))
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
}
