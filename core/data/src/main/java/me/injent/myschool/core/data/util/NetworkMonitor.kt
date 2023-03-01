package me.injent.myschool.core.data.util

import kotlinx.coroutines.flow.Flow

/**
 * Checks internet connection and if connected returns flow of true
 */
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}