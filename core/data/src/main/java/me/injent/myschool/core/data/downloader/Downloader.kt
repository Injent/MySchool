package me.injent.myschool.core.data.downloader

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface Downloader {
    fun downloadFile(
        url: String,
        preferredFileName: String,
        onDownloadComplitionListener: OnDownloadComplitionListener
    )
}

fun interface OnDownloadComplitionListener {
    fun onComplete(success: Boolean)
}

class AndroidDownloader @Inject constructor(
    @ApplicationContext context: Context
) : Downloader {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    private var downloadId: Long = -1
    private var onCompletionListener: OnDownloadComplitionListener? = null

    init {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (downloadId == id) {
                    onCompletionListener?.onComplete(true)
                    context.unregisterReceiver(this)
                    onCompletionListener = null
                } else {
                    onCompletionListener?.onComplete(false)
                }
            }
        }
        context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun downloadFile(
        url: String,
        preferredFileName: String,
        onDownloadComplitionListener: OnDownloadComplitionListener
    ) {
        onCompletionListener = onDownloadComplitionListener

        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        val finalFileName = "$preferredFileName.$extension"

        val request = DownloadManager.Request(url.toUri())
            .setMimeType(mimeType)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(finalFileName)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                finalFileName
            )
        downloadId = downloadManager.enqueue(request)
    }
}