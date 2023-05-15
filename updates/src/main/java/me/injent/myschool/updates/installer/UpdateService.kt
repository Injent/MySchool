package me.injent.myschool.updates.installer

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import androidx.annotation.IntRange
import androidx.core.net.toFile
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import me.injent.myschool.core.common.network.Dispatcher
import me.injent.myschool.core.common.network.MsDispatchers.IO
import me.injent.myschool.core.common.util.provideUri
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.updates.R
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject


/**
 * Service used for downloading apk
 */
@AndroidEntryPoint
class UpdateService : Service() {

    @Inject
    @Dispatcher(IO)
    lateinit var ioDispatcher: CoroutineDispatcher
    private val scope: CoroutineScope
        get() = CoroutineScope(ioDispatcher)
    @Inject
    lateinit var userDataRepository: dagger.Lazy<UserDataRepository>
    private val serviceId: Int
        get() = "update_service".hashCode()
    private lateinit var updateApkFile: File

    override fun onCreate() {
        super.onCreate()
        this.updateApkFile = applicationContext.updateApkFile
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val apkUrl = intent.getStringExtra(KEY_APK_URL)
            ?: throw NullPointerException("Intent doesn't contains $KEY_APK_URL")
        scope.launch {
            downloadUpdate(
                sourceUrl = apkUrl,
                destination = updateApkFile.toUri()
            )
            stopUpdateService()
        }

        startForeground(
            serviceId,
            this.updateNotification(
                getString(R.string.downloading_update), getString(R.string.open_app)
            )
        )
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        showUpdateDownloadCompletedNotification(updateApkFile.provideUri(this))
        super.onDestroy()
    }

    private fun sendUpdateDownloadData(status: Int, @IntRange(from = 0, to = 100) progress: Int) {
        val intent = Intent(ACTION_SEND_UPDATE_DOWNLOAD_DATA).apply {
            putExtra(KEY_STATUS, status)
            putExtra(KEY_PROGRESS, progress)
        }
        sendBroadcast(intent)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun downloadUpdate(
        sourceUrl: String,
        destination: Uri
    ) = withContext(ioDispatcher) {
        sendUpdateDownloadData(STATUS_RUNNING, 0)
        try {
            val url = URL(sourceUrl)
            val urlConnection = url.openConnection().apply {
                connect()
            }
            val totalBytes = urlConnection.contentLength
            var downloadedBytes: Int

            BufferedInputStream(url.openStream()).use { input ->
                FileOutputStream(destination.toFile()).use { output ->
                    val data = ByteArray(4096)
                    var current: Long = 0
                    while (input.read(data).also { downloadedBytes = it } != -1) {
                        current += downloadedBytes.toLong()
                        sendUpdateDownloadData(
                            STATUS_RUNNING, ((current * 100) / totalBytes).toInt()
                        )
                        output.write(data, 0, downloadedBytes)
                    }
                }
            }
            sendUpdateDownloadData(STATUS_SUCCESS, 100)
        } catch (e: Exception) {
            e.printStackTrace()
            sendUpdateDownloadData(STATUS_FAILED, 0)
        }
    }
}