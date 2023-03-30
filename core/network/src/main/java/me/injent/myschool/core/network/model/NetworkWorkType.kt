package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkWorkType(
    val id: Long,
    val kind: String,
    val title: String,
    val abbr: String
)
