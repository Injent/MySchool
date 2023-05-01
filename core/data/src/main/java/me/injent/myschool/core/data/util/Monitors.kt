package me.injent.myschool.core.data.util

import kotlinx.coroutines.flow.Flow
import me.injent.myschool.core.common.sync.SyncState
import me.injent.myschool.core.data.downloader.Downloader

/**
 * Checks internet connection and if connected returns flow of true
 */
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}

interface SyncProgressMonitor {
    val progress: Flow<Int>
}

/**
 * Checks when data is synchronizing and returns flow of true if yes
 */
interface SyncStatusMonitor {
    val isSyncing: Flow<SyncState>
}