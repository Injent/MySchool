package me.injent.myschool.core.data.repository

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import me.injent.myschool.core.data.model.asEntity
import me.injent.myschool.core.data.util.RepoDependency
import me.injent.myschool.core.database.dao.PersonDao
import me.injent.myschool.core.database.model.PersonEntity
import me.injent.myschool.core.database.model.asExternalModel
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.NetworkPerson
import javax.inject.Inject

@RepoDependency(UserDataRepository::class)
interface PersonRepository : Syncable {
    fun getPersonsStream(): Flow<List<Person>>
    fun getPerson(personId: Long): Flow<Person?>
}

class OfflineFirstPersonRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val personDao: PersonDao,
    private val userDataSource: MsPreferencesDataSource
) : PersonRepository {
    override suspend fun synchronize(): Boolean = coroutineScope {
        return@coroutineScope try {
            val myUserId = userDataSource.userData.first().userContext!!.userId

            val userIds = networkDataSource.getClassmates() + myUserId

            val persons = mutableListOf<NetworkPerson>()
            for (userId in userIds) {
                try {
                    persons.add(networkDataSource.getPerson(userId))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            personDao.savePersons(persons.map(NetworkPerson::asEntity))
            true
        } catch (e: Exception) {
            false
        }
    }
    override fun getPersonsStream(): Flow<List<Person>> =
        personDao.getPersons().map { it.map(PersonEntity::asExternalModel) }

    override fun getPerson(personId: Long): Flow<Person?> =
        personDao.getPerson(personId).map { it?.asExternalModel() }
}