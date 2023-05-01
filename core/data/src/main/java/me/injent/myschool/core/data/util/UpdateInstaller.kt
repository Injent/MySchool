package me.injent.myschool.core.data.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transformWhile
import me.injent.myschool.core.common.network.Dispatcher
import me.injent.myschool.core.common.network.MsDispatchers.IO
import me.injent.myschool.core.data.downloader.Downloader
import me.injent.myschool.core.data.downloader.MsDownloader
import me.injent.myschool.core.data.downloader.MsDownloaderType.Android
import me.injent.myschool.core.data.downloader.MsDownloaderType.Internal
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateInstaller @Inject constructor(
    @ApplicationContext private val context: Context,
    @MsDownloader(Android) private val downloader: Downloader,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun update(url: String) = withContext(ioDispatcher) {
        val downloadId = downloader.downloadFile(
            sourceUrl = url,
            preferredFileName = "update"
        )
        Log.e("ENDED", "SASA")
        downloader.getStatus(downloadId)
            .onEach {
                Log.e("PROGRESS", it.toString())
                if (it == Downloader.Status.SUCCESS)
                    openUpdateApk(downloader.getDownloadedFileUri(downloadId))
            }
            .collect()
    }

    private fun openUpdateApk(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(
            uri,
            "application/vnd.android.package-archive"
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }

    private fun getUriFromFile(filePath: String): Uri {
        return if (SDK_INT < 24) {
            Uri.fromFile(File(filePath))
        } else {
            FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                File(filePath)
            )
        }
    }
}