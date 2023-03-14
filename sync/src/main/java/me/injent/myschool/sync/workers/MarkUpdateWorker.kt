package me.injent.myschool.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.injent.myschool.core.common.network.Dispatcher
import me.injent.myschool.core.common.network.MsDispatchers.IO
import me.injent.myschool.core.data.repository.MarkRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.sync.initializers.SyncConstraints
import java.time.Duration

@HiltWorker
class MarkUpdateWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val userDataRepository: UserDataRepository,
    private val markRepository: MarkRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        try {
            val isSynchronized = listOf(
                userDataRepository.synchronize(),
                markRepository.synchronize()
            ).all { it }

            if (isSynchronized) {
                Result.success()
            }
            Result.retry()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }

    companion object {
        fun startUpMarkUpdateWork() = PeriodicWorkRequestBuilder<MarkUpdateWorker>(Duration.ofSeconds(40))
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .build()
    }
}