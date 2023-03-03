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
    @Query("SELECT * FROM persons WHERE id = :userId LIMIT 1")
    suspend fun getPersonByUserId(userId: Long): PersonEntity?
    @Query("DELETE FROM persons")
    suspend fun deleteAll()
}