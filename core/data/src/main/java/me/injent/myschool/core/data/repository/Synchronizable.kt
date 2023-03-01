package me.injent.myschool.core.data.repository

/**
 * Interface for repositories that synchronize data between local and remote data sources
 */
interface Synchronizable {
    suspend fun synchronize()
}