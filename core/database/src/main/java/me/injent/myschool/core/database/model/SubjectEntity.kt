package me.injent.myschool.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.injent.myschool.core.model.Subject

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey
    val id: Long,
    val name: String
)

fun SubjectEntity.asExternalModel() = Subject(
    id = id,
    name = name
)