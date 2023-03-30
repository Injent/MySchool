package me.injent.myschool.core.data.model

import me.injent.myschool.core.database.model.MarkEntity
import me.injent.myschool.core.network.model.NetworkMark

fun NetworkMark.asEntity(dbSubjectId: Long) = MarkEntity(
    id = id,
    value = value,
    date = date,
    personId = personId,
    workId = workId,
    lessonId = lessonId,
    mood = mood,
    dbSubjectId = dbSubjectId
)