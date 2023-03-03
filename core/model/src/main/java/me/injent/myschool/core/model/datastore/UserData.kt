package me.injent.myschool.core.model.datastore

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userContext: SaveableUserContext? = null,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val lastSyncTime: LocalDateTime? = null
)