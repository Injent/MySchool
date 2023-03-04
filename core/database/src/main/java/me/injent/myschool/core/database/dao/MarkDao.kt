package me.injent.myschool.core.database.dao

import androidx.room.Dao
import androidx.room.Upsert
import me.injent.myschool.core.database.model.MarkEntity

@Dao
interface MarkDao {
    @Upsert
    suspend fun saveMarks(marks: List<MarkEntity>)
}