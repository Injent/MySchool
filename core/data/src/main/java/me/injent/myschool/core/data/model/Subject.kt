package me.injent.myschool.core.data.model

import me.injent.myschool.core.database.model.SubjectEntity
import me.injent.myschool.core.datastore.model.SaveableSubject
import me.injent.myschool.core.network.model.NetworkHomeworkSubject
import me.injent.myschool.core.network.model.NetworkSubject

fun NetworkSubject.asEntity() = SubjectEntity(
    id = id,
    name = name
)

fun NetworkHomeworkSubject.asSaveableModel() = SaveableSubject(
    id = id,
    name = name
)

fun NetworkSubject.asSaveableModel() = SaveableSubject(
    id = id,
    name = name
)