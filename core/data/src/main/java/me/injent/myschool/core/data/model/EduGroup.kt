package me.injent.myschool.core.data.model

import me.injent.myschool.core.datastore.model.SaveableEduGroup
import me.injent.myschool.core.network.model.NetworkEduGroup

fun NetworkEduGroup.asSaveableModel() = SaveableEduGroup(
    id = id,
    name = name,
    studyYear = studyYear,
    timetable = timetable
)