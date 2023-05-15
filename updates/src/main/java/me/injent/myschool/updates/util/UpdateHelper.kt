package me.injent.myschool.updates.installer

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import me.injent.myschool.updates.R
import java.io.File

internal const val ACTION_SEND_UPDATE_DOWNLOAD_DATA = "SEND_UPDATE_DOWNLOAD_DATA"
const val ACTION_UPDATE_DOWNLOADED = "UPDATE_DOWNLOADED"

const val STATUS_FAILED = 0
const val STATUS_RUNNING = 1
const val STATUS_SUCCESS = 2

internal const val KEY_APK_URL = "apk_url"
internal const val KEY_STATUS = "status"
internal const val KEY_PROGRESS = "progress"

private const val UpdateNotificationChannelId = "update_channel"
private val UpdateDownloadNotificationId: Int
    get() = "update_notification".hashCode()

internal val Context.notificationManager: NotificationManager
    get() = this.getSystemService(NotificationManager::class.java)

internal fun Context.updateNotification(
    title: String,
    description: String,
    apkUri: Uri? = null
): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            UpdateNotificationChannelId,
            getString(R.string.update_channel_name),
            NotificationManager.IMPORTANCE_HIGH,
        )

        notificationManager.createNotificationChannel(channel)
    }

    val updateIntent = Intent(
        ACTION_UPDATE_DOWNLOADED,
        apkUri,
        this,
        Class.forName("$packageName.MainActivity")
    )

    val pending: PendingIntent = TaskStackBuilder.create(this).run {
        addNextIntentWithParentStack(updateIntent)
        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
    }
    return NotificationCompat.Builder(
        this, UpdateNotificationChannelId
    )
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentTitle(title)
        .setContentText(description)
        .setSmallIcon(R.drawable.ic_update)
        .setContentIntent(pending)
        .build()
}

fun Context.startUpdateService(url: String) {
    ContextCompat.startForegroundService(
        this,
        Intent(this, UpdateService::class.java).apply {
            putExtra(KEY_APK_URL, url)
        }
    )
}

fun Context.stopUpdateService() {
    stopService(Intent(this, UpdateService::class.java))
}

@SuppressLint("MissingPermission")
internal fun Context.showUpdateDownloadCompletedNotification(apkUri: Uri) {
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        NotificationManagerCompat.from(this)
            .notify(
                UpdateDownloadNotificationId,
                updateNotification(
                    getString(R.string.update_download_completed),
                    getString(R.string.press_to_install),
                    apkUri
                )
            )
    }
}

fun Context.openUpdateApk(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(
        uri,
        "application/vnd.android.package-archive"
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    startActivity(intent)
}

val Context.updateApkFile: File
    get() = File(cacheDir, "update.apk")