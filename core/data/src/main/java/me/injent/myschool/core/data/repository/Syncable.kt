package me.injent.myschool.core.data.repository

/**
 * Interface for repositories that synchronize data between local and remote data sources
 */
interface Syncable {
    /**
     * Starts syncrhonization in all [Syncable] repositories
     * Returns true if successful
     */
    suspend fun synchronize(): Boolean
}