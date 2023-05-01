package me.injent.myschool.updates.installer

import android.net.Uri
import androidx.core.net.toFile
import kotlinx.coroutines.*
import me.injent.myschool.core.common.network.Dispatcher
import me.injent.myschool.core.common.network.MsDispatchers.IO
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateInstaller @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun downloadUpdate(
        sourceUrl: String,
        destination: Uri,
        onDataChange: (status: Int, progress: Int) -> Unit
    ) = withContext(ioDispatcher) {
        onDataChange(STATUS_RUNNING, 0)
        try {
            val url = URL(sourceUrl)
            val urlConnection = url.openConnection().apply {
                connect()
            }
            val total = urlConnection.contentLength
            var count: Int

            BufferedInputStream(url.openStream()).use { input ->
                FileOutputStream(destination.toFile()).use { output ->
                    val data = ByteArray(4096)
                    var current: Long = 0
                    while (input.read(data).also { count = it } != -1) {
                        current += count.toLong()
                        onDataChange(STATUS_RUNNING, ((current * 100) / total).toInt())
                        output.write(data, 0, count)
                    }
                }
            }
            onDataChange(STATUS_SUCCESS, 100)
        } catch (e: Exception) {
            e.printStackTrace()
            onDataChange(STATUS_FAILED, 0)
        }
    }
}