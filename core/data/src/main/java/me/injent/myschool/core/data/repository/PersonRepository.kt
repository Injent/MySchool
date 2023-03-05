package me.injent.myschool.core.data.repository

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Semaphore
import me.injent.myschool.core.data.model.asEntity
import me.injent.myschool.core.database.dao.PersonDao
import me.injent.myschool.core.database.model.PersonEntity
import me.injent.myschool.core.database.model.asExternalModel
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.NetworkPerson
import me.injent.myschool.core.network.model.asExternalModel
import javax.inject.Inject

interface PersonRepository : Syncable {
    fun getPersonsStream(): Flow<List<Person>>
    fun getPerson(personId: Long): Flow<Person?>
}

class OfflineFirstPersonRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val personDao: PersonDao,
    private val preferencesDataSource: MsPreferencesDataSource
) : PersonRepository {
    override fun getPersonsStream(): Flow<List<Person>>
        = personDao.getPersons().map { it.map(PersonEntity::asExternalModel) }

    override fun getPerson(personId: Long): Flow<Person?>
        = personDao.getPerson(personId).map { it?.asExternalModel() }

    override suspend fun synchronize(): Boolean = coroutineScope {
        return@coroutineScope try {
            val userContext = networkDataSource.getUserContext().asExternalModel()
            preferencesDataSource.setUserContext(userContext)
            // Obtain users ids from current user class
            val userIds = networkDataSource.getClassmates() + userContext.userId
            val semaphore = Semaphore(permits = maxConcurrentRequests)

            val persons = mutableListOf<NetworkPerson>()
            for (userId in userIds) {
                // Use maximum of threads avalaible to use
                // This increases delivery speed of network data
                semaphore.acquire()
                try {
                    // Gets person data and adds it to list
                    persons.add(networkDataSource.getPerson(userId))
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    semaphore.release()
                }
            }
            personDao.savePersons(persons.map(NetworkPerson::asEntity))
            true
        } catch (e: Exception) {
            false
        }
    }
}