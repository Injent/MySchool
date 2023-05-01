package me.injent.myschool.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.injent.myschool.core.database.model.PersonEntity

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
    @Query("SELECT * FROM persons WHERE person_id = :personId LIMIT 1")
    fun getPersonStream(personId: Long): Flow<PersonEntity?>
    @Query("SELECT * FROM persons WHERE person_id = :personId LIMIT 1")
    suspend fun getPerson(personId: Long): PersonEntity?
    @Query("SELECT person_id FROM persons WHERE person_id != :myPersonId")
    suspend fun getClassmatesPersonIds(myPersonId: Long): List<Long>
    @Query("SELECT id FROM persons WHERE person_id != :myPersonId")
    suspend fun getClassmatesUserIds(myPersonId: Long): List<Long>
    @Query("DELETE FROM persons")
    suspend fun deleteAll()
}