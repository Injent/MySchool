package me.injent.myschool.core.model.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val token: String? = null,
    val userContext: SaveableUserContext? = null
)