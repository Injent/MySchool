package me.injent.myschool.core.model.datastore

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userContext: SaveableUserContext? = null,
    val lastSyncTime: LocalDateTime? = null,
    val bannedSubjects: List<Long> = emptyList()
)