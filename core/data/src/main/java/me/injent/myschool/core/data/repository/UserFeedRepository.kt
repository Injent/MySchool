package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import me.injent.myschool.core.database.dao.PersonDao
import me.injent.myschool.core.model.Birthday
import me.injent.myschool.core.model.UserFeed
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.asExternalModel
import javax.inject.Inject

interface UserFeedRepository {
    suspend fun getUserFeed(
        groupId: Long,
        personId: Long,
    ): UserFeed

    suspend fun getClosestBirthdays(from: LocalDate, limit: Int = 1): List<Birthday>
}

class RemoteUserFeedRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource,
    private val personDao: PersonDao
) : UserFeedRepository {
    override suspend fun getUserFeed(groupId: Long, personId: Long): UserFeed =
        networkDataSource.getUserFeed(groupId, personId).asExternalModel()

    override suspend fun getClosestBirthdays(from: LocalDate, limit: Int): List<Birthday> {
        return personDao.getPersons().first()
            .filter { person ->
                with(person.birthday) {
                    this != null && monthNumber >= from.monthNumber &&
                            !(from.monthNumber == monthNumber && dayOfMonth < from.dayOfMonth)
                }
            }
            .map { person ->
                Birthday(
                    personId = person.personId,
                    personName = person.shortName,
                    date = person.birthday!!,
                    daysUntil = from.daysUntil(
                        with(person.birthday!!) {
                            LocalDate(from.year, monthNumber, dayOfMonth)
                        }
                    )
                )
            }
            .sortedBy(Birthday::daysUntil)
            .take(limit)
    }
}