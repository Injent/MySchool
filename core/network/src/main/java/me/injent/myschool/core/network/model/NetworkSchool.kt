package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.School

@Serializable
data class NetworkSchool(
    val id: Int,
    val name: String,
    val type: String,
    val groupIds: List<Long>
)

fun NetworkSchool.asExternalModel() = School(
    id = id,
    name = name,
    type = type
)