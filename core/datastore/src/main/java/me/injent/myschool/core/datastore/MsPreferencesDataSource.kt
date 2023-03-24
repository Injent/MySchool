package me.injent.myschool.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import me.injent.myschool.core.common.util.atTimeZone
import me.injent.myschool.core.common.util.currentLocalDateTime
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.model.datastore.UserData
import javax.inject.Inject

class MsPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<UserData>
) {
    val userData = dataStore.data

    suspend fun updateMarksSyncDateTime() {
        dataStore.updateData {
            it.copy(
                lastMarksSyncDateTime =
                    LocalDateTime.currentLocalDateTime().atTimeZone(TimeZone.UTC)
            )
        }
    }

    suspend fun setUserContext(userContext: UserContext) {
        dataStore.updateData {
            it.copy(userContext = userContext)
        }
    }

    suspend fun setInitizalized() {
        dataStore.updateData {
            it.copy(isInitialized = true)
        }
    }
}