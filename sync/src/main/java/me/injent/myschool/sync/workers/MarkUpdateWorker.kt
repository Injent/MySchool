package me.injent.myschool.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.injent.myschool.core.common.network.Dispatcher
import me.injent.myschool.core.common.network.MsDispatchers.IO
import me.injent.myschool.core.data.repository.MarkRepository
import me.injent.myschool.core.data.repository.ReceivedMarksResult
import me.injent.myschool.sync.initializers.MarkUpdateWorkConstraints
import me.injent.myschool.sync.initializers.markUpdateForegroundInfo
import me.injent.myschool.sync.initializers.showMarkUpdateNotification
import java.time.Duration

@HiltWorker
class MarkUpdateWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val markRepository: MarkRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        try {
            val isSynchronized = markRepository.receiveClassmatesMarks()

            if (!isSynchronized)
                return@withContext Result.failure()

            when (val result = markRepository.receiveNewMarks()) {
                ReceivedMarksResult.Error -> {
                    Result.retry()
                }
                is ReceivedMarksResult.MultipleMarks -> {
                    for (mark in result.marks) {
                        appContext.showMarkUpdateNotification(
                            content = "${mark.subjectName} ${mark.value}",
                            markId = mark.markId
                        )
                    }
                    Result.success()
                }
                is ReceivedMarksResult.NewMark -> {
                    appContext.showMarkUpdateNotification(
                        content = "${result.subjectName} ${result.value}",
                        markId = result.markId
                    )
                    Result.success()
                }
                ReceivedMarksResult.NotChanged -> Result.success()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.markUpdateForegroundInfo()

    companion object {
        const val WorkName = "mark_receiving"
        private val workInterval: Duration
            get() = Duration.ofMinutes(15)

        fun startUpMarkUpdateWork() =
            PeriodicWorkRequestBuilder<MarkUpdateWorker>(workInterval)
                .setConstraints(MarkUpdateWorkConstraints)
                .build()
    }
}