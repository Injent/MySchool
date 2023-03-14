package me.injent.myschool.core.data.model

import me.injent.myschool.core.datastore.model.SaveableHomework
import me.injent.myschool.core.datastore.model.SaveableHomeworkData
import me.injent.myschool.core.datastore.model.SaveableHomeworkFile
import me.injent.myschool.core.network.model.*

fun NetworkHomeworkData.asSaveableModel() = SaveableHomeworkData(
    homeworks = homeworks.map(NetworkHomework::asSaveableModel),
    files = files.map(NetworkAttachment::asSaveableModel),
    subjects = subjects.map(NetworkHomeworkSubject::asSaveableModel)
)

fun NetworkHomework.asSaveableModel() = SaveableHomework(
    text = text,
    subjectId = subjectId,
    files = files
)

fun NetworkAttachment.asSaveableModel() = SaveableHomeworkFile(
    id = id,
    type = type,
    name = name,
    downloadUrl = downloadUrl,
    size = size
)