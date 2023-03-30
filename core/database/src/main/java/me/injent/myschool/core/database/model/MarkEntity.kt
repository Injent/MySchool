package me.injent.myschool.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.model.Mark
import kotlin.random.Random

@Entity(tableName = "marks")
data class MarkEntity(
    @PrimaryKey
    val id: Long,
    val value: String,
    val date: LocalDateTime,
    @ColumnInfo(name = "person_id")
    val personId: Long,
    @ColumnInfo(name = "work_id")
    val workId: Long,
    @ColumnInfo(name = "lesson_id")
    val lessonId: Long?,
    val mood: Mark.Mood,
    @ColumnInfo(name = "subject_id")
    val dbSubjectId: Long
)

fun MarkEntity.asExternalModel() = Mark(
    id = id,
    value = value,
    date = date,
    personId = personId,
    workId = workId,
    lessonId = lessonId,
    mood = mood,
    dbSubjectId = dbSubjectId
)
