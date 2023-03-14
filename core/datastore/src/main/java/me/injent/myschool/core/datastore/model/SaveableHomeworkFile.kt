package me.injent.myschool.core.datastore.model

import me.injent.myschool.core.model.FileType
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.Attachment

@Serializable
data class SaveableHomeworkFile(
    val id: Long,
    val type: FileType,
    val name: String,
    val downloadUrl: String,
    val size: Long
)

fun SaveableHomeworkFile.asExternalModel() = Attachment(
    id = id,
    type = type,
    name = name,
    downloadUrl = downloadUrl,
    size = size
)

