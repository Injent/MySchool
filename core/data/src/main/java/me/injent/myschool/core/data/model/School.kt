package me.injent.myschool.core.data.model

import me.injent.myschool.core.datastore.model.SaveableSchool
import me.injent.myschool.core.network.model.NetworkSchool

fun NetworkSchool.asSaveableModel() = SaveableSchool(
    id = id,
    name = name,
    type = type
)