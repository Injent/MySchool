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
import me.injent.myschool.core.common.sync.SyncState
import me.injent.myschool.sync.initializers.SyncWorkName
import javax.inject.Inject

/**
 * Uses for cheking synhronization state
 */
class SyncWorkStatusMonitor @Inject constructor(
    @ApplicationContext context: Context,
) : SyncStatusMonitor {
    override val isSyncing: Flow<SyncState> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(SyncWorkName)
            .map {
                if (it.isEmpty()) return@map SyncState.IDLE
                return@map when (it.first().state) {
                    WorkInfo.State.RUNNING -> SyncState.SYNCING
                    WorkInfo.State.SUCCEEDED -> SyncState.SUCCESS
                    else -> SyncState.IDLE
                }
            }
            .asFlow()
            .conflate()
}
