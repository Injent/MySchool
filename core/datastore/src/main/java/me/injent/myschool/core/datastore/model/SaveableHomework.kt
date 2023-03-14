package me.injent.myschool.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class SaveableHomework(
    val text: String,
    val subjectId: Long,
    val files: List<Long>
)