package me.injent.myschool.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.EduGroup
import me.injent.myschool.core.network.IdSerializer

@Serializable
data class NetworkEduGroup(
    @SerialName("id_str")
    @Serializable(IdSerializer::class)
    val id: Long,
    val name: String,
    val timetable: Long,
    @SerialName("studyyear") val studyYear: Int
)

fun NetworkEduGroup.asExternalModel() = EduGroup(
    id = id,
    name = name,
    timetable = timetable,
    studyYear = studyYear
)