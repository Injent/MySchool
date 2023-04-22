package me.injent.myschool.core.domain

import me.injent.myschool.core.database.util.ClearDatabaseProvider
import javax.inject.Inject

class ClearDatabaseUseCase @Inject constructor(
    private val clearDatabaseProvider: ClearDatabaseProvider
) {
    suspend operator fun invoke() = clearDatabaseProvider()
}