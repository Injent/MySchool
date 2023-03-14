package me.injent.myschool.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class SaveableHomeworkData(
    val homeworks: List<SaveableHomework> = emptyList(),
    val files: List<SaveableHomeworkFile> = emptyList(),
    val subjects: List<SaveableSubject> = emptyList()
)