package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.Hours

@Serializable
data class NetworkHours(
    val startHour: String,
    val startMinute: String,
    val endHour: String,
    val endMinute: String
) {
    val interval: String
        get() = "$startHour:$startMinute-$endHour:$endMinute"
}

fun NetworkHours.asExternalModel() = Hours(
    startHour, startMinute, endHour, endMinute
)