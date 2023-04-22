package me.injent.myschool.sync.monitor

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import me.injent.myschool.core.data.util.SyncProgressMonitor
import me.injent.myschool.sync.workers.SyncWorker
import javax.inject.Inject

internal const val KEY_PROGRESS = "progress"

class SyncWorkProgressMonitor @Inject constructor(
    @ApplicationContext context: Context
) : SyncProgressMonitor {
    override val progress: Flow<Int> = WorkManager.getInstance(context)
        .getWorkInfosForUniqueWorkLiveData(SyncWorker.WorkName)
        .map {
            if (it.isEmpty()) {
                0
            } else {
                it.first().progress.getInt(KEY_PROGRESS, 0)
            }
        }
        .asFlow()
        .conflate()
}