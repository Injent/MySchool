package me.injent.myschool.core.data.model

import me.injent.myschool.core.database.model.SubjectEntity
import me.injent.myschool.core.network.model.NetworkSubject

fun NetworkSubject.asEntity() = SubjectEntity(
    id = id,
    name = name
)