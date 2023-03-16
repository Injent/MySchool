package me.injent.myschool.core.model

import kotlinx.serialization.Serializable

/**
 * Information about school
 */
@Serializable
data class School(
    val id: Long,
    val name: String,
    val type: String,
    val avatarUrl: String? = null
)