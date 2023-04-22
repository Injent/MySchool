package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkHomeworkFilesReponse(
    val files: List<NetworkAttachment>
)
