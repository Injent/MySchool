package me.injent.myschool.core.data.util

import kotlinx.coroutines.flow.Flow

interface SyncProgressMonitor {
    val progress: Flow<Int>
}