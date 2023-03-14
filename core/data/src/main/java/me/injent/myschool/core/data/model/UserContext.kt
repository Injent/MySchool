package me.injent.myschool.core.data.model

import me.injent.myschool.core.datastore.model.SaveableUserContext
import me.injent.myschool.core.network.model.NetworkUserContext

fun NetworkUserContext.asSaveableModel() = SaveableUserContext(
    userId = userId,
    personId = personId,
    shortName = shortName,
    school = schools.first().asSaveableModel(),
    eduGroup = eduGroups.first().asSaveableModel()
)