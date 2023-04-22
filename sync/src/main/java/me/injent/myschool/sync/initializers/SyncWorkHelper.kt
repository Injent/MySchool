package me.injent.myschool.sync.initializers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import me.injent.myschool.sync.R

internal val SyncConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

internal fun Context.syncForegroundInfo(): ForegroundInfo {
    val notification = NotificationCompat.Builder(
        this,
        SilentChannelId,
    )
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(getString(R.string.sync_notification_title))
        .setPriority(NotificationCompat.PRIORITY_MIN)
        .build()

    return ForegroundInfo(
        SilentNotificationId,
        notification,
    )
}