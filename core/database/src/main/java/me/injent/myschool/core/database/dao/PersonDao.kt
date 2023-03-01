package me.injent.myschool.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import me.injent.myschool.core.database.model.PersonEntity

/**
 * [Dao] for [PersonEntity]
 */
@Dao
interface PersonDao {
    @Upsert
    suspend fun savePerson(person: PersonEntity)

    @Query("DELETE FROM people")
    suspend fun deleteAll()
}