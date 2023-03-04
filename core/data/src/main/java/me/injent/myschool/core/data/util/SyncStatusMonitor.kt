package me.injent.myschool.core.data.util

import kotlinx.coroutines.flow.Flow
import me.injent.myschool.core.common.sync.SyncState

/**
 * Checks when data is synchronizing and returns flow of true if yes
 */
interface SyncStatusMonitor {
    val isSyncing: Flow<SyncState>
}
