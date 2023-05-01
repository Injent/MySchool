package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.model.Period
import me.injent.myschool.core.model.datastore.UserData
import javax.inject.Inject

interface UserDataRepository {
    val userData: Flow<UserData>
    suspend fun clear()
    suspend fun updateMarksSyncDateTime()
    suspend fun setInitizalized()
    suspend fun selectPeriod(period: Period)
    suspend fun ignoreUpdate(ignore: Boolean)
}

class UserDataRepositoryImpl @Inject constructor(
    private val userDataSource: MsPreferencesDataSource
) : UserDataRepository {
    override suspend fun clear() =
        userDataSource.clear()

    override val userData: Flow<UserData> =
        userDataSource.userData
    override suspend fun updateMarksSyncDateTime() =
        userDataSource.updateMarksSyncDateTime()
    override suspend fun setInitizalized() =
        userDataSource.setInitizalized()

    override suspend fun selectPeriod(period: Period) =
        userDataSource.selectPeriod(period)

    override suspend fun ignoreUpdate(ignore: Boolean) =
        userDataSource.ignoreUpdate(ignore)
}