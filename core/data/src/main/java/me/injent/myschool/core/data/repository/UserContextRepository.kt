package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.model.UserContext
import javax.inject.Inject

interface UserContextRepository {
    val userContext: Flow<UserContext?>
    suspend fun setUserContext(userContext: UserContext)
}

class OfflineFirstUserContextRepository @Inject constructor(
    private val userDataSource: MsPreferencesDataSource
) : UserContextRepository {

    override val userContext: Flow<UserContext?>
        get() = userDataSource.userData.map { it.userContext }

    override suspend fun setUserContext(userContext: UserContext) =
        userDataSource.setUserContext(userContext)
}