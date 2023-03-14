package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.injent.myschool.core.common.util.currentLocalDateTime
import me.injent.myschool.core.data.model.asSaveableModel
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.datastore.model.*
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.model.datastore.UserData
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import javax.inject.Inject

interface UserDataRepository : Syncable {
    val userData: Flow<UserData>
    suspend fun getUserContext(): UserContext?
    suspend fun setUserContext(userContext: SaveableUserContext)
    suspend fun updateSyncTime()
    suspend fun banSubject(subject: SaveableSubject)
    suspend fun setInitizalized()
}

class UserDataRepositoryImpl @Inject constructor(
    private val userDataSource: MsPreferencesDataSource,
    private val networkDataSource: DnevnikNetworkDataSource
) : UserDataRepository {

    override suspend fun synchronize(): Boolean = try {
        val userContext = networkDataSource.getUserContext()
        userDataSource.setUserContext(userContext.asSaveableModel())

        val eduGroupId = userContext.eduGroups.first().id
        val currentTime = LocalDateTime.currentLocalDateTime()
        val period = networkDataSource.getReportingPeriods(eduGroupId).find {
            it.start <= currentTime && it.finish >= currentTime
        }!!
        userDataSource.setCurrentPeriod(period.asSaveableModel())
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    override val userData: Flow<UserData>
        = userDataSource.userData.map(SaveableUserData::asExternalModel)
    override suspend fun getUserContext(): UserContext? = runBlocking {
        userDataSource.userData.first().userContext?.asExternalModel()
    }
    override suspend fun updateSyncTime() =
        userDataSource.setLastSyncTime()
    override suspend fun setUserContext(userContext: SaveableUserContext) =
        userDataSource.setUserContext(userContext)

    override suspend fun banSubject(subject: SaveableSubject) =
        userDataSource.banSubject(subject)
    override suspend fun setInitizalized() =
        userDataSource.setInitizalized()
}