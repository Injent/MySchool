package me.injent.myschool.sync

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import me.injent.myschool.sync.workers.MarkUpdateWorker
import me.injent.myschool.sync.workers.SyncWorker
import javax.inject.Inject

class WorkHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun pruneWork() {
        WorkManager.getInstance(context).pruneWork()
    }

    fun startOneTimeSyncWork() {
        WorkManager.getInstance(context).enqueueUniqueWork(
            SyncWorker.WorkName,
            ExistingWorkPolicy.KEEP,
            SyncWorker.startUpSyncWork()
        )
    }

    fun startPeriodicMarkUpdateWork() {
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            MarkUpdateWorker.WorkName,
            ExistingPeriodicWorkPolicy.KEEP,
            MarkUpdateWorker.startUpMarkUpdateWork()
        )
    }
}