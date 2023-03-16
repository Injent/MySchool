package me.injent.myschool.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.injent.myschool.core.network.IdSerializer

@Serializable
data class UserIdResponse(
    @SerialName("id_str")
    @Serializable(IdSerializer::class)
    val id: Long
)
