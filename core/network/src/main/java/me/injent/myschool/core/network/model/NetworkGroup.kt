package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.Group

@Serializable
data class NetworkGroup(
    val id: Long,
    @Serializable
    val type: Group.Type? = null,
    val name: String
)

fun NetworkGroup.asExternalModel() = Group(
    id = id,
    name = name,
    type = type
)
