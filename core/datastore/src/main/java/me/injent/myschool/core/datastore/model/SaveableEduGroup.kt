package me.injent.myschool.core.datastore.model

import me.injent.myschool.core.model.EduGroup

@kotlinx.serialization.Serializable
data class SaveableEduGroup(
    val id: Long,
    val name: String,
    val studyYear: Int,
    val timetable: Long
)

fun SaveableEduGroup.asExternalModel() = EduGroup(
    id = id,
    name = name,
    timetable = timetable,
    studyYear = studyYear
)