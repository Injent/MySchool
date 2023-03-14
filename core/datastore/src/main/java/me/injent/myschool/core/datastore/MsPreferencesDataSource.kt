package me.injent.myschool.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import me.injent.myschool.core.datastore.model.*
import javax.inject.Inject

class MsPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<SaveableUserData>
) {
    val userData = dataStore.data

    suspend fun setLastSyncTime() {
        dataStore.updateData {
            val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            it.copy(lastSyncTime = time)
        }
    }

    suspend fun setUserContext(userContext: SaveableUserContext) {
        dataStore.updateData {
            it.copy(userContext = userContext)
        }
    }

    suspend fun banSubject(subject: SaveableSubject) {
        dataStore.updateData {
            if (it.bannedSubjects.contains(subject)) return@updateData it
            it.copy(bannedSubjects = it.bannedSubjects + subject)
        }
    }

    suspend fun setInitizalized() {
        dataStore.updateData {
            it.copy(isInitialized = true)
        }
    }

    suspend fun setCurrentPeriod(period: SaveableReportingPeriod) {
        dataStore.updateData {
            it.copy(period = period)
        }
    }
}