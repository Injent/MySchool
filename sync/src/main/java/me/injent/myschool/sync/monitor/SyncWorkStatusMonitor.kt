package me.injent.myschool.sync.monitor

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import me.injent.myschool.core.data.util.SyncStatusMonitor
import me.injent.myschool.sync.initializers.WORKER_NAME
import javax.inject.Inject

/**
 * Uses for cheking synhronization state
 */
class SyncWorkStatusMonitor @Inject constructor(
    @ApplicationContext context: Context,
) : SyncStatusMonitor {
    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(WORKER_NAME)
            .map(MutableList<WorkInfo>::anyRunning)
            .asFlow()
            .conflate()
}

private val List<WorkInfo>.anyRunning get() = any { it.state == WorkInfo.State.RUNNING }
