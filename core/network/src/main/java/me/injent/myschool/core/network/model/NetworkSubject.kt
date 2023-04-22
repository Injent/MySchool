package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.Subject

@Serializable
data class NetworkSubject constructor(
    val id: Long,
    val name: String
)

fun NetworkSubject.asExternalModel() = Subject(
    id = id,
    name = name
)