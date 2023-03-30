package me.injent.myschool.sync

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import me.injent.myschool.sync.workers.MarkUpdateWorker
import me.injent.myschool.sync.workers.SyncWorker

fun WorkManager.startOneTimeSyncWork() {
    this.enqueueUniqueWork(
            SyncWorker.WorkName,
            ExistingWorkPolicy.KEEP,
            SyncWorker.startUpSyncWork()
        )
}

fun WorkManager.startPeriodicMarkUpdateWork() {
    this.enqueueUniquePeriodicWork(
        MarkUpdateWorker.WorkName,
        ExistingPeriodicWorkPolicy.KEEP,
        MarkUpdateWorker.startUpMarkUpdateWork()
    )
}