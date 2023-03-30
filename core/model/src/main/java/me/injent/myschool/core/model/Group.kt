package me.injent.myschool.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: Long,
    val name: String,
    @Serializable
    val type: Type? = null
) {
    enum class Type {
        Group,
        Subgroup
    }
}
