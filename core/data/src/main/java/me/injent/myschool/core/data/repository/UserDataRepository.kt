package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.model.datastore.UserData
import javax.inject.Inject

interface UserDataRepository {
    val userData: Flow<UserData>
    suspend fun setToken(token: String?)
    suspend fun setUserContext(userContext: UserContext)
}

class UserDataRepositoryImpl @Inject constructor(
    private val userDataSource: MsPreferencesDataSource
) : UserDataRepository {
    override val userData: Flow<UserData>
        = userDataSource.userData
    override suspend fun setToken(token: String?)
        = userDataSource.setToken(token)
    override suspend fun setUserContext(userContext: UserContext)
        = userDataSource.setUserContext(userContext)
}