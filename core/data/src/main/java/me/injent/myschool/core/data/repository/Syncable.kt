package me.injent.myschool.core.data.repository

/**
 * Interface for repositories that synchronize data between local and remote data sources
 */
interface Syncable {
    val maxConcurrentRequests
        get() = 2

    /**
     * Starts syncrhonization in all [Syncable] repositories
     * Returns true if successful
     */
    suspend fun synchronize(): Boolean
}