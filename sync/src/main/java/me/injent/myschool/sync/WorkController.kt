package me.injent.myschool.sync

import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import me.injent.myschool.sync.initializers.SyncWorkName
import me.injent.myschool.sync.workers.SyncWorker
import javax.inject.Inject

class WorkController @Inject constructor(
    private val workManager: WorkManager
) {
    fun startOneTimeSyncWork() {
        workManager
            .enqueueUniqueWork(
                SyncWorkName,
                ExistingWorkPolicy.KEEP,
                SyncWorker.startUpSyncWork()
            )
    }
}