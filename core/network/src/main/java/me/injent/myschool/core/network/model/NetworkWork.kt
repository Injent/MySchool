package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.Work
import me.injent.myschool.core.network.IdSerializer

@Serializable
data class NetworkWork(
    @SerialName("id_str")
    @Serializable(IdSerializer::class)
    val id: Long,
    @SerialName("lesson")
    val lessonId: Long,
    val text: String,
    val subjectId: Long,
    val targetDate: LocalDateTime,
    val sentDate: LocalDateTime?,
    val status: String,
    val type: String
)

fun NetworkWork.asExternalModel() = Work(
    id = id,
    lessonId = lessonId,
    text = text,
    subjectId = subjectId,
    targetDate = targetDate,
    sentDate = sentDate,
    status = status,
    type = type
)
