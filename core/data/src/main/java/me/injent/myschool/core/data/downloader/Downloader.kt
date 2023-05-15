package me.injent.myschool.core.data.downloader

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import javax.inject.Qualifier

/**
 * The [Downloader] is a tool class which can be provided by Hilt with annotation MsDownloader.
 * It handles long-running HTTP downloads
 */
interface Downloader {
    fun getProgress(downloadId: Long): Flow<Int>
    fun getStatus(downloadId: Long): Flow<Status>

    fun downloadFile(
        sourceUrl: String,
        preferredFileName: String
    ): Long

    fun getDownloadedFileUri(downloadId: Long): Uri

    enum class Status {
        SUCCESS,
        FAILED,
        RUNNING
    }
}