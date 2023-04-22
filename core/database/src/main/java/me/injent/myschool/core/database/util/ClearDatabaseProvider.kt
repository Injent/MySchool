package me.injent.myschool.core.database.util

import me.injent.myschool.core.database.MsDatabase
import javax.inject.Inject

class ClearDatabaseProvider @Inject constructor(
    private val database: MsDatabase
) {
    @Suppress("RedundantSuspendModifier")
    suspend operator fun invoke() {
        database.clearAllTables()
    }
}