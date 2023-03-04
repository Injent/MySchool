package me.injent.myschool.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.injent.myschool.core.database.model.SubjectEntity

@Dao
interface SubjectDao {
    @Upsert
    suspend fun saveSubjects(subjects: List<SubjectEntity>)
    @Query("SELECT * FROM subjects")
    fun getSubjects(): Flow<List<SubjectEntity>>
}