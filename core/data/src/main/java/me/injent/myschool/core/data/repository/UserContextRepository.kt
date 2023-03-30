package me.injent.myschool.core.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.network.ApiProvider
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import javax.inject.Inject

interface UserContextRepository : Syncable {
    val userContext: Flow<UserContext?>
}

class OfflineFirstUserContextRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val userDataSource: MsPreferencesDataSource
) : UserContextRepository {
    override suspend fun synchronize(): Boolean = try {
        val userId = networkDataSource.getMyUserId().id
        val userContext = networkDataSource.getContextPerson(userId).getUserContext()

        userDataSource.setUserContext(userContext)
        true
    } catch (e: Exception) {
        Log.e("UserContextRepository", "Failed to sync")
        e.printStackTrace()
        false
    }

    override val userContext: Flow<UserContext?>
        get() = userDataSource.userData.map { it.userContext }
}