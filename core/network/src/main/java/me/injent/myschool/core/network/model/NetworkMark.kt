package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.Mark
import me.injent.myschool.core.network.MarkSerializer
import me.injent.myschool.core.network.IdSerializer

@Serializable
data class NetworkMark(
    @SerialName("id_str")
    @Serializable(IdSerializer::class)
    val id: Long,
    //@Serializable(MarkSerializer::class)
    val value: String,
    val date: LocalDateTime,
    @SerialName("person_str")
    @Serializable(IdSerializer::class)
    val personId: Long,
    @SerialName("work_str")
    @Serializable(IdSerializer::class)
    val workId: Long,
    @SerialName("lesson_str")
    @Serializable(IdSerializer::class)
    val lessonId: Long
)

fun NetworkMark.asExternalModel(dbSubjectId: Long? = null) = Mark(
    id = id,
    value = value,
    date = date,
    personId = personId,
    workId = workId,
    lessonId = lessonId,
    dbSubjectId = dbSubjectId
)
