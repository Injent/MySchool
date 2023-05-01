package me.injent.myschool.core.data.downloader

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MsDownloader(val type: MsDownloaderType)

enum class MsDownloaderType {
    Android,
    Internal
}

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