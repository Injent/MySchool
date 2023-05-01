package me.injent.myschool.core.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.injent.myschool.core.data.model.asEntity
import me.injent.myschool.core.data.util.RepoDependency
import me.injent.myschool.core.database.dao.PersonDao
import me.injent.myschool.core.database.model.PersonEntity
import me.injent.myschool.core.database.model.asExternalModel
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.asExternalModel
import javax.inject.Inject
import kotlin.math.roundToInt

interface PersonRepository : Syncable {
    /**
     * Stream of list of [Person]
     */
    val persons: Flow<List<Person>>

    /**
     * Stream of [Person]
     * @param personId id of the person looking for
     */
    fun getPersonStream(personId: Long): Flow<Person?>
    fun getPersonByUserId(userId: Long): Flow<Person?>
    suspend fun getAvatarUrl(userId: Long): String?
    suspend fun savePersons(userIds: Array<Long>)
}

@RepoDependency(UserContextRepository::class)
class OfflineFirstPersonRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val personDao: PersonDao,
    private val userDataSource: MsPreferencesDataSource
) : PersonRepository {
    override suspend fun synchronize(onProgress: ((Int) -> Unit)?): Boolean = try {
        var progress = 0
        val userContext = userDataSource.userData.first().userContext!!
        val userIds = networkDataSource.getClassmates() + userContext.userId

        val contacts = networkDataSource.getChatContacts()
        val persons = userIds.mapNotNull { userId ->
            try {
                val person = networkDataSource.getPerson(userId)
                progress++
                val percentage = ((progress.toFloat() / userIds.size) * 100).roundToInt().coerceIn(0, 100)
                onProgress?.invoke(percentage)
                person
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }.map { person ->
            val contact = contacts.contacts.find { it.userId == person.id }
            if (person.personId == userContext.personId) {
                person.asEntity().copy(
                    shortName = "${userContext.lastName} ${userContext.firstName}",
                    avatarUrl = userContext.avatarUrl
                )
            } else {
                person.asEntity().copy(
                    shortName = contact?.name ?: person.shortName,
                    avatarUrl = networkDataSource.getAvatarUrl(person.id)
                )
            }
        }
        personDao.savePersons(persons)
        true
    } catch (e: Exception) {
        Log.e("SubjectRepository", "Failed to sync")
        e.printStackTrace()
        false
    }

    override val persons: Flow<List<Person>>
        get() = personDao.getPersons().map { it.map(PersonEntity::asExternalModel) }

    override fun getPersonStream(personId: Long): Flow<Person?> =
        personDao.getPersonStream(personId).map { it?.asExternalModel() }

    override fun getPersonByUserId(userId: Long): Flow<Person?> = flow {
        emit(networkDataSource.getPerson(userId)?.asExternalModel())
    }

    override suspend fun getAvatarUrl(userId: Long): String? {
        return networkDataSource.getAvatarUrl(userId)
    }

    override suspend fun savePersons(userIds: Array<Long>) {
        val contacts = networkDataSource.getChatContacts()
        val personEntities = userIds.mapNotNull { userId ->
            networkDataSource.getPerson(userId)?.run {
                val contact = contacts.contacts.find { it.userId == userId }
                this.asEntity().copy(
                    shortName = contact?.name ?: this.shortName,
                    avatarUrl = networkDataSource.getAvatarUrl(userId)
                )
            }
        }
        personDao.savePersons(personEntities)
    }
}