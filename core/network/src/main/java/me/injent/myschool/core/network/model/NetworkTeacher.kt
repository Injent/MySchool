package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.Teacher

@Serializable
data class NetworkTeacher(
    val id: Long,
    val shortName: String,
)

fun NetworkTeacher.asExternalModel() = Teacher(
    id = id,
    shortName = shortName
)