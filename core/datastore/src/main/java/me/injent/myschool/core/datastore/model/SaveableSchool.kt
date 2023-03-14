package me.injent.myschool.core.datastore.model

import me.injent.myschool.core.model.School

@kotlinx.serialization.Serializable
data class SaveableSchool(
    val id: Int,
    val name: String,
    val type: String
)

fun SaveableSchool.asExternalModel() = School(
    id = id,
    name = name,
    type = type
)