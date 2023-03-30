package me.injent.myschool.core.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.common.util.currentLocalDateTime
import me.injent.myschool.core.data.model.asEntity
import me.injent.myschool.core.data.util.RepoDependency
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
    val persons: Flow<List<Person>>
    fun getPerson(personId: Long): Flow<Person?>
    fun getPersonByUserId(userId: Long): Flow<Person?>
    fun getClosestBirthdays(limit: Int): Flow<Map<String, LocalDate>>
}

@RepoDependency(UserContextRepository::class)
class OfflineFirstPersonRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val personDao: PersonDao,
    private val userDataSource: MsPreferencesDataSource
) : PersonRepository {
    override suspend fun synchronize(): Boolean = try {
        val myUserId = userDataSource.userData.first().userContext!!.userId
        val userIds = networkDataSource.getClassmates() + myUserId

        val persons = mutableListOf<NetworkPerson>()
        for (userId in userIds) {
            try {
                networkDataSource.getPerson(userId)?.run {
                    persons.add(this)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        personDao.savePersons(persons.map(NetworkPerson::asEntity))
        true
    } catch (e: Exception) {
        Log.e("SubjectRepository", "Failed to sync")
        e.printStackTrace()
        false
    }

    override val persons: Flow<List<Person>>
        get() = personDao.getPersons().map { it.map(PersonEntity::asExternalModel) }

    override fun getPerson(personId: Long): Flow<Person?> =
        personDao.getPerson(personId).map { it?.asExternalModel() }

    override fun getPersonByUserId(userId: Long): Flow<Person?> = flow {
        emit(networkDataSource.getPerson(userId)?.asExternalModel())
    }

    override fun getClosestBirthdays(limit: Int): Flow<Map<String, LocalDate>> {
        val currentDate = LocalDateTime.currentLocalDateTime().date
        return personDao.getClosestBirthdays()
            .map { flow ->
                flow.map { (person, birthday) ->
                    if (birthday != null &&
                        birthday.monthNumber >= currentDate.monthNumber &&
                        birthday.dayOfMonth >= currentDate.dayOfMonth) {
                        Pair(person, birthday)
                    } else {
                        null
                    }
                }
                    .filterNotNull()
                    .sortedBy { (_, date) -> date.toEpochDays() }
                    .take(limit)
                    .toMap()
            }
    }
}