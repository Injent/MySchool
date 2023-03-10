package me.injent.myschool.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
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
    fun getPerson(personId: Long): Flow<PersonEntity?>
    @Query("DELETE FROM persons")
    suspend fun deleteAll()
}