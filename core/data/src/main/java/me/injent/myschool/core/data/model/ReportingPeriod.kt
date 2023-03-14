package me.injent.myschool.core.data.model

import me.injent.myschool.core.datastore.model.SaveableReportingPeriod
import me.injent.myschool.core.network.model.NetworkReportingPeriod

fun NetworkReportingPeriod.asSaveableModel() = SaveableReportingPeriod(
    id = id,
    name = name,
    number = number,
    start = start,
    finish = finish
)