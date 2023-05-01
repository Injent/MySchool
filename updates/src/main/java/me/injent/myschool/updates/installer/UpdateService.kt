package me.injent.myschool.updates.installer

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import androidx.annotation.IntRange
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.injent.myschool.core.common.util.provideUri
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.updates.BuildConfig
import me.injent.myschool.updates.R
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class UpdateService : Service() {

    private val scope: CoroutineScope
        get() = CoroutineScope(Dispatchers.Default)

    private val serviceId: Int
        get() = "update_service".hashCode()

    private lateinit var updateApkUri: Uri

    @Inject
    lateinit var installer: dagger.Lazy<UpdateInstaller>
    @Inject
    lateinit var userDataRepository: dagger.Lazy<UserDataRepository>

    override fun onCreate() {
        super.onCreate()
        updateApkUri = updateApkFile.provideUri(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val apkUrl = intent.getStringExtra(KEY_APK_URL)
            ?: throw NullPointerException("Intent doesn't contains $KEY_APK_URL")
        scope.launch {
            installer.get().downloadUpdate(
                sourceUrl = apkUrl,
                destination = updateApkUri,
                onDataChange = { status, progress ->
                    sendUpdateDownloadData(status, progress)
                    if (status == STATUS_FAILED || status == STATUS_SUCCESS) {
                        stopService(Intent(applicationContext, UpdateService::class.java))
                    }
                }
            )
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
        showUpdateDownloadCompletedNotification(updateApkUri)
        super.onDestroy()
    }

    private fun sendUpdateDownloadData(status: Int, @IntRange progress: Int) {
        val intent = Intent(ACTION_SEND_UPDATE_DOWNLOAD_DATA).apply {
            putExtra(KEY_STATUS, status)
            putExtra(KEY_PROGRESS, progress)
        }
        sendBroadcast(intent)
    }
}