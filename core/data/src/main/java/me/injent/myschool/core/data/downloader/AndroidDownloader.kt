package me.injent.myschool.core.data.downloader

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidDownloader @Inject constructor(
    @ApplicationContext private val context: Context
) : Downloader {
    private val contentResolver = context.contentResolver
    private val observers: MutableMap<Long, ContentObserver> = mutableMapOf()
    private val downloadManager by lazy {
        context.getSystemService(DownloadManager::class.java)
    }

    override fun getDownloadedFileUri(downloadId: Long): Uri =
        downloadManager.getUriForDownloadedFile(downloadId)

    @SuppressLint("Range")
    override fun getProgress(downloadId: Long): Flow<Int> = callbackFlow {
        registerContentObserver(downloadId) {
            val query = DownloadManager.Query()
                .setFilterById(downloadId)
            downloadManager.query(query).use {
                it.moveToFirst()
                val totalBytes = it.getInt(
                    it.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                )
                val downloadedBytes = it.getInt(
                    it.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                )
                val precentage = downloadedBytes * 100 / totalBytes
                trySend(precentage)

                if (precentage == 100) {
                    channel.close()
                }
            }
        }
        awaitClose { unregisterContentObserver(downloadId) }
    }.distinctUntilChanged()

    @SuppressLint("Range")
    override fun getStatus(downloadId: Long): Flow<Downloader.Status> = callbackFlow {
        registerContentObserver(downloadId) {
            val query = DownloadManager.Query()
                .setFilterById(downloadId)
            downloadManager.query(query).use {
                it.moveToFirst()
                val status = when (
                    it.getInt(
                        it.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    )
                ) {
                    DownloadManager.STATUS_SUCCESSFUL -> Downloader.Status.SUCCESS
                    DownloadManager.STATUS_RUNNING -> Downloader.Status.RUNNING
                    else -> Downloader.Status.FAILED
                }
                trySend(status)

                if (status == Downloader.Status.SUCCESS || status == Downloader.Status.FAILED) {
                    close()
                }
            }
        }
        awaitClose { unregisterContentObserver(downloadId) }
    }.distinctUntilChanged()

    override fun downloadFile(
        sourceUrl: String,
        preferredFileName: String
    ): Long {
        val extension = MimeTypeMap.getFileExtensionFromUrl(sourceUrl)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        val finalFileName = "$preferredFileName.$extension"

        val request = DownloadManager.Request(sourceUrl.toUri())
            .setTitle(finalFileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setMimeType(mimeType)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                finalFileName
            )

        return downloadManager.enqueue(request)
    }

    private fun registerContentObserver(downloadId: Long, onChange: () -> Unit) {
        if (observers[downloadId] != null) return

        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
                onChange()
            }
        }
        observers[downloadId] = observer
        contentResolver.registerContentObserver(
            getDownloadingFileUri(downloadId), false, observer
        )
    }

    private fun unregisterContentObserver(downloadId: Long) {
        observers[downloadId]?.let {
            contentResolver.unregisterContentObserver(it)
            observers.remove(downloadId)
        }
    }

    @SuppressLint("Range")
    private fun getDownloadingFileUri(downloadId: Long): Uri {
        return "content://downloads/all_downloads/$downloadId".toUri()
    }
}