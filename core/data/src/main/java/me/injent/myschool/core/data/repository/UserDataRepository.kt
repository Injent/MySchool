package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.model.datastore.UserData
import me.injent.myschool.core.model.datastore.asExternalModel
import javax.inject.Inject

interface UserDataRepository {
    val userData: Flow<UserData>
    suspend fun getUserContext(): UserContext?
    suspend fun updateSyncTime()
    suspend fun setUserContext(userContext: UserContext)
}

class UserDataRepositoryImpl @Inject constructor(
    private val userDataSource: MsPreferencesDataSource
) : UserDataRepository {
    override val userData: Flow<UserData>
        = userDataSource.userData

    override suspend fun getUserContext(): UserContext? = runBlocking {
        userData.first().userContext?.asExternalModel()
    }

    override suspend fun updateSyncTime()
        = userDataSource.setLastSyncTime()
    override suspend fun setUserContext(userContext: UserContext)
        = userDataSource.setUserContext(userContext)
}