package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.Mark

@Serializable
data class NetworkMark(
    val id: Long,
    val value: String,
    val date: LocalDateTime,
    @SerialName("person")
    val personId: Long,
    @SerialName("work")
    val workId: Long,
    @SerialName("lesson")
    val lessonId: Long?,
    @Serializable
    val mood: Mark.Mood
)

fun NetworkMark.asExternalModel(dbSubjectId: Long? = null) = Mark(
    id = id,
    value = value,
    date = date,
    personId = personId,
    workId = workId,
    lessonId = lessonId,
    mood = mood,
    dbSubjectId = dbSubjectId
)
