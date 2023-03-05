package me.injent.myschool.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.injent.myschool.core.database.model.MarkEntity

@Dao
interface MarkDao {
    @Upsert
    suspend fun saveMarks(marks: List<MarkEntity>)
    @Query("SELECT value FROM marks WHERE person_id = :personId AND subject_id = :subjectId")
    suspend fun getPersonMarkValuesBySubject(personId: Long, subjectId: Long): List<String>
    @Query("SELECT * FROM marks WHERE person_id = :personId AND subject_id = :subjectId")
    fun getPersonMarkBySubject(personId: Long, subjectId: Long): Flow<List<MarkEntity>>

    @Query("SELECT value FROM marks WHERE person_id = :personId")
    fun getPersonAverageMark(personId: Long): Flow<List<String>>
}