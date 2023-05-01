package me.injent.myschool.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import me.injent.myschool.core.common.util.atTimeZone
import me.injent.myschool.core.common.util.currentLocalDateTime
import me.injent.myschool.core.model.Period
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.model.datastore.UserData
import javax.inject.Inject

class MsPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<UserData>
) {
    val userData = dataStore.data

    suspend fun clear() {
        dataStore.updateData {
            UserData()
        }
    }

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
            it.copy(
                userContext = userContext,
                selectedPeriod = userContext.reportingPeriodGroup.periods.find(Period::isCurrent)
                    ?: userContext.reportingPeriodGroup.periods.firstOrNull()
            )
        }
    }

    suspend fun setInitizalized() {
        dataStore.updateData {
            it.copy(isInitialized = true)
        }
    }

    suspend fun selectPeriod(period: Period) {
        dataStore.updateData {
            it.copy(selectedPeriod = period)
        }
    }

    suspend fun ignoreUpdate(ignore: Boolean) {
        dataStore.updateData {
            it.copy(ignoreUpdate = ignore)
        }
    }
}