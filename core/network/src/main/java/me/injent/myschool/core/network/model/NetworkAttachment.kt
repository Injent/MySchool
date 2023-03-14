package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.FileType
import me.injent.myschool.core.model.Attachment

@Serializable
data class NetworkAttachment(
    val id: Long,
    val type: FileType,
    val name: String,
    val downloadUrl: String,
    val size: Long
)

fun NetworkAttachment.asExternalModel() = Attachment(
    id = id,
    type = type,
    name = name,
    downloadUrl = downloadUrl,
    size = size
)