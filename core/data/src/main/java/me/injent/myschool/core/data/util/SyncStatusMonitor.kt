package me.injent.myschool.core.data.util

import kotlinx.coroutines.flow.Flow

/**
 * Checks when data is synchronizing and returns flow of true if yes
 */
interface SyncStatusMonitor {
    val isSyncing: Flow<Boolean>
}
