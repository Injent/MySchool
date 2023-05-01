package me.injent.myschool.feature.updatedialog

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transformWhile
import me.injent.myschool.core.common.network.Dispatcher
import me.injent.myschool.core.common.network.MsDispatchers.IO
import me.injent.myschool.core.data.downloader.Downloader
import java.io.File
import javax.inject.Inject

class UpdateInstaller @Inject constructor(
    @ApplicationContext val context: Context,
    private val downloader: Downloader,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {

     suspend fun update(url: String) {
        val downloadId = downloader.downloadFile(url, "update")

        withContext(ioDispatcher) {
            downloader.getStatus(downloadId)
                .transformWhile {
                    emit(it)
                    it != DownloadManager.STATUS_SUCCESSFUL && it != DownloadManager.STATUS_FAILED
                }
                .onEach {
                    if (it == DownloadManager.STATUS_SUCCESSFUL) {
                        openUpdateApk(downloader.getDownloadedFileUri(downloadId))
                    }
                }
                .collect()
        }
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