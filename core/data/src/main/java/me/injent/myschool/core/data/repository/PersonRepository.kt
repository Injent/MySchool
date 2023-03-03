package me.injent.myschool.core.data.repository

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Semaphore
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.asExternalModel
import me.injent.myschool.core.common.result.Result
import me.injent.myschool.core.common.result.Result.Error
import me.injent.myschool.core.common.result.Result.Success
import me.injent.myschool.core.data.model.asEntity
import me.injent.myschool.core.database.dao.PersonDao
import me.injent.myschool.core.database.model.PersonEntity
import me.injent.myschool.core.database.model.asExternalModel
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.network.model.NetworkPerson
import javax.inject.Inject
import kotlin.system.measureTimeMillis

interface PersonRepository : Synchronizable {
    suspend fun getContext(): Result<UserContext>
    fun getPersonsStream(): Flow<List<Person>>
    suspend fun getPerson(userId: Long): Person?
}

class OfflineFirstPersonRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val personDao: PersonDao
) : PersonRepository {
    override fun getPersonsStream(): Flow<List<Person>>
        = personDao.getPersons().map { it.map(PersonEntity::asExternalModel) }

    override suspend fun getPerson(userId: Long): Person?
        = personDao.getPersonByUserId(userId)?.asExternalModel()

    override suspend fun synchronize(): Boolean = coroutineScope {
        return@coroutineScope try {
            // Obtain users ids from current user class
            val userIds = networkDataSource.getClassmates()
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
            // Save to local source
            Log.e("Persons", persons.size.toString())
            personDao.savePersons(persons.map(NetworkPerson::asEntity))
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getContext(): Result<UserContext> {
        val result = networkDataSource.getUserContext()
        return if (result is Success) {
            Success(result.data.asExternalModel())
        } else {
            // There is no way Result.Loading can be received
            // so the force cast is used here
            Error((result as Error).exception)
        }
    }
}