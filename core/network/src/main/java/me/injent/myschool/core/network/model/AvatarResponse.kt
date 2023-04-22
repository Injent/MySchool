package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AvatarResponse(
    val avatar: Content
) {
    @Serializable
    data class Content(
        val url: String?
    )
}
