package me.injent.myschool.core.data.downloader

import android.content.Context
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import me.injent.myschool.core.data.downloader.Downloader.Status
import java.io.*
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton
import androidx.annotation.IntRange
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import me.injent.myschool.core.common.network.Dispatcher
import me.injent.myschool.core.common.network.MsDispatchers.IO

@Singleton
class InternalStorageDownloader @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : Downloader {

    private class Process(
        var uri: Uri,
        var status: Status,
        @IntRange(from = 0, to = 100) var progress: Int = 0,
        var onChange: (() -> Unit)? = null
    ) {
        fun notifyChanges() {
            Log.e("NOTIFY", "SA")
            onChange?.invoke()
        }
    }

    private val processes: MutableMap<Long, Process> = mutableMapOf()

    @Suppress("BlockingMethodInNonBlockingContext")
    override fun downloadFile(
        sourceUrl: String,
        preferredFileName: String
    ): Long {
        val downloadId = sourceUrl.hashCode().toLong()
        val file = File(context.cacheDir, preferredFileName)
        processes[downloadId] = Process(file.toUri(), Status.RUNNING, 0)

        CoroutineScope(ioDispatcher).launch {
            try {
                val url = URL(sourceUrl)
                val urlConnection = url.openConnection().apply {
                    connect()
                }
                val total = urlConnection.contentLength
                var count: Int

                BufferedInputStream(url.openStream()).use { input ->
                    FileOutputStream(file).use { output ->
                        val data = ByteArray(4096)
                        var current: Long = 0
                        while (input.read(data).also { count = it } != -1) {
                            current += count.toLong()
                            processes[downloadId]?.progress = ((current * 100) / total).toInt()
                            processes[downloadId]?.notifyChanges()
                            output.write(data, 0, count)
                        }
                    }
                }
                processes[downloadId]?.status = Status.SUCCESS
                processes[downloadId]?.notifyChanges()
            } catch (e: Exception) {
                e.printStackTrace()
                processes[downloadId]?.status = Status.FAILED
                processes[downloadId]?.notifyChanges()
            }
        }
        return downloadId
    }

    override fun getProgress(downloadId: Long): Flow<Int> = callbackFlow {
        processes[downloadId]?.onChange = {
            processes[downloadId]?.progress?.run {
                trySend(this)
            }
        }
        awaitClose { processes[downloadId]?.onChange = null }
    }.distinctUntilChanged()

    override fun getStatus(downloadId: Long): Flow<Status> = callbackFlow {
        val process = processes[downloadId]
        if (process == null) {
            close()
            return@callbackFlow
        }
        processes[downloadId]?.onChange = {
            processes[downloadId]?.status?.run {
                trySend(this)
                if (this == Status.SUCCESS || this == Status.FAILED) {
                    close()
                }
            }
        }
        trySend(process.status)
        awaitClose { processes.remove(downloadId) }
    }.distinctUntilChanged()

    override fun getDownloadedFileUri(downloadId: Long): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            processes[downloadId]?.uri?.toFile() ?: throw NullPointerException()
        )
    }
}