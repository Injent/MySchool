package me.injent.myschool.core.datastore

import androidx.datastore.core.DataStore
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.model.datastore.UserData
import me.injent.myschool.core.model.datastore.toSaveableModel
import javax.inject.Inject

class MsPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<UserData>
) {
    val userData = dataStore.data

    suspend fun setToken(token: String?) {
        dataStore.updateData {
            it.copy(token = token)
        }
    }

    suspend fun setUserContext(userContext: UserContext) {
        dataStore.updateData {
            it.copy(userContext = userContext.toSaveableModel())
        }
    }
}