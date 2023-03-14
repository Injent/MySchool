package me.injent.myschool.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.Subject
import me.injent.myschool.core.network.IdSerializer

@Serializable
data class NetworkSubject constructor(
    @SerialName("id_str")
    @Serializable(IdSerializer::class)
    val id: Long,
    val name: String
)

@Serializable
data class NetworkHomeworkSubject constructor(
    val id: Long,
    val name: String
)

fun NetworkHomeworkSubject.asExternalModel() = Subject(
    id = id,
    name = name
)

fun NetworkSubject.asExternalModel() = Subject(
    id = id,
    name = name
)