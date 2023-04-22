package me.injent.myschool.core.database.dao

import androidx.room.Dao
import androidx.room.MapInfo
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import me.injent.myschool.core.database.model.PersonEntity
import me.injent.myschool.core.database.model.PersonIdAndName

/**
 * [Dao] for [PersonEntity]
 */
@Dao
interface PersonDao {
    @Upsert
    suspend fun savePerson(person: PersonEntity)
    @Upsert
    suspend fun savePersons(list: List<PersonEntity>)
    @Query("SELECT * FROM persons")
    fun getPersons(): Flow<List<PersonEntity>>
    @Query("SELECT person_id, short_name FROM persons")
    fun getPersonsIdAndName(): List<PersonIdAndName>
    @Query("SELECT * FROM persons WHERE person_id = :personId LIMIT 1")
    fun getPersonStream(personId: Long): Flow<PersonEntity?>
    @Query("SELECT * FROM persons WHERE person_id = :personId LIMIT 1")
    suspend fun getPerson(personId: Long): PersonEntity?
    @MapInfo(keyColumn = "short_name", valueColumn = "birthday")
    @Query("SELECT short_name, birthday FROM persons WHERE birthday NOT NULL AND birthday >= :from AND birthday <= :to ORDER BY ABS(birthday - :from) ASC LIMIT :limit")
    suspend fun getClosestBirthdays(from: LocalDate, to: LocalDate, limit: Int = 3): Map<String, LocalDate>
    @Query("SELECT person_id FROM persons WHERE person_id != :myPersonId")
    suspend fun getClassmatesPersonIds(myPersonId: Long): List<Long>
    @Query("DELETE FROM persons")
    suspend fun deleteAll()
}