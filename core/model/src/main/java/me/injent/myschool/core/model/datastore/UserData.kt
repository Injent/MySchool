package me.injent.myschool.core.model.datastore

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.UserContext

@Serializable
data class UserData(
    val userContext: UserContext? = null,
    val lastSyncTime: LocalDateTime? = null,
    val isInitialized: Boolean = false
)