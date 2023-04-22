package me.injent.myschool.core.model

data class Hours(
    val startHour: String,
    val startMinute: String,
    val endHour: String,
    val endMinute: String
) {
    val interval: String
        get() = "$startHour:$startMinute-$endHour:$endMinute"
}