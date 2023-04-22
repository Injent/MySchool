package me.injent.myschool.sync.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import me.injent.myschool.core.common.network.Dispatcher
import me.injent.myschool.core.common.network.MsDispatchers.IO
import me.injent.myschool.core.data.repository.*
import me.injent.myschool.core.domain.ClearDatabaseUseCase
import me.injent.myschool.sync.initializers.SyncConstraints
import me.injent.myschool.sync.initializers.syncForegroundInfo
import me.injent.myschool.sync.monitor.KEY_PROGRESS

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher,
    private val personRepository: PersonRepository,
    private val subjectRepository: SubjectRepository,
    private val markRepository: MarkRepository,
    private val userDataRepository: UserDataRepository,
    private val clearDatabaseUseCase: ClearDatabaseUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(dispatcher) {
        return@withContext try {
            clearDatabaseUseCase.invoke()

            val progress = mutableMapOf(
                "person" to 0,
                "subject" to 0,
                "mark" to 0
            )

            val progressEmitterJob = launch(Dispatchers.Default + SupervisorJob()) {
                while (isActive) {
                    setProgress(
                        Data.Builder()
                            .putInt(
                                KEY_PROGRESS,
                                progress.values.sum() / progress.values.size
                            )
                            .build()
                    )
                    delay(100)
                }
            }
            val isSynchronized = listOf(
                personRepository.synchronize { percentage ->
                    progress["person"] = percentage
                    Log.e("Sync", percentage.toString())
                },
                subjectRepository.synchronize { percentage ->
                    progress["subject"] = percentage
                },
                markRepository.synchronize { percentage ->
                    progress["mark"] = percentage
                },
            ).all { it }

            progressEmitterJob.cancelAndJoin()
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