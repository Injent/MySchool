package me.injent.myschool.core.data.model

import me.injent.myschool.core.database.model.PersonEntity
import me.injent.myschool.core.network.model.NetworkPerson

fun NetworkPerson.asEntity() = PersonEntity(
    id = id,
    shortName = shortName,
    locale = locale,
    birthday = birthday,
    sex = sex,
    roles = roles,
    phone = phone
)