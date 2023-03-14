package me.injent.myschool.core.datastore.model

import me.injent.myschool.core.model.Subject

@kotlinx.serialization.Serializable
data class SaveableSubject(
    val id: Long,
    val name: String
)

fun SaveableSubject.asExternalModel() = Subject(
    id = id,
    name = name
)