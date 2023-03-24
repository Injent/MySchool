package me.injent.myschool.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.database.model.MarkEntity

@Dao
interface MarkDao {
    @Upsert
    suspend fun saveMarks(marks: List<MarkEntity>)
    @Upsert
    suspend fun saveMark(mark: MarkEntity)

    @Query("SELECT * FROM marks WHERE id = :markId LIMIT 1")
    fun getMark(markId: Long): Flow<MarkEntity>
    @Query("SELECT ROUND(AVG(CAST(value AS INTEGER)), 2) FROM marks WHERE person_id = :personId AND subject_id = :subjectId AND value GLOB '[0-9]*'")
    suspend fun getPersonAverageMarkBySubject(personId: Long, subjectId: Long): Float
    @Query("SELECT * FROM marks WHERE person_id = :personId AND subject_id = :subjectId ORDER BY date")
    fun getPersonMarkBySubject(personId: Long, subjectId: Long): Flow<List<MarkEntity>>
    @Query("SELECT ROUND(AVG(CAST(value AS INTEGER)), 2) FROM marks WHERE person_id = :personId AND value NOT NULL AND value GLOB '[0-9]*'")
    suspend fun getPersonAverageMark(personId: Long): Float
    @Query("DELETE FROM marks WHERE date < :currentDateOfPeriod")
    suspend fun deleteDeprecatedMarks(currentDateOfPeriod: LocalDateTime)
    @Query("SELECT EXISTS(SELECT id FROM marks WHERE id = :markId LIMIT 1)")
    suspend fun contains(markId: Long): Boolean
    @Query("SELECT ROUND(AVG(CAST(value AS INTEGER)), 2) FROM marks WHERE person_id = :personId AND value NOT NULL AND value GLOB '[0-9]*' AND date >= :startDateTime AND date <= :endDateTime")
    suspend fun getPersonAverageMark(
        personId: Long,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Float

    @Query("SELECT ROUND(AVG(CAST(value AS INTEGER)), 2) FROM marks WHERE person_id = :personId AND value NOT NULL AND value GLOB '[0-9]*' AND date <= :beforeDate")
    suspend fun getPersonAverageMark(
        personId: Long,
        beforeDate: LocalDateTime,
    ): Float
}