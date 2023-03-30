package me.injent.myschool.core.network.model.auth

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.EpochLocalDateTimeSerializer

@Serializable
data class EsiaLoginResponse(
    val accessTokenval: String,
    @Serializable(EpochLocalDateTimeSerializer::class)
    val expiresIn: LocalDateTime,
    val refreshToken: String,
    val scope: String,
    @SerialName("user")
    val userId: Long
)