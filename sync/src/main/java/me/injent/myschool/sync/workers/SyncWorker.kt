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
import me.injent.myschool.core.data.repository.*
import me.injent.myschool.sync.initializers.SyncConstraints
import me.injent.myschool.sync.initializers.syncForegroundInfo

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher,
    private val personRepository: PersonRepository,
    private val subjectRepository: SubjectRepository,
    private val markRepository: MarkRepository,
    private val userDataRepository: UserDataRepository,
    private val userContextRepository: UserContextRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(dispatcher) {
        return@withContext try {
            val isSynchronized = listOf(
                userContextRepository.synchronize(),
                personRepository.synchronize(),
                subjectRepository.synchronize(),
                markRepository.synchronize(),
            ).all { it }

            return@withContext if (isSynchronized) {
                userDataRepository.setInitizalized()
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    override suspend fun getForegroundInfo() =
        appContext.syncForegroundInfo()

    companion object {
        const val WorkName = "synchronization"
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<SyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .build()
    }
}