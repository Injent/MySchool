package me.injent.myschool.core.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

class InstantConverter {
    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? =
        instant?.toEpochMilliseconds()
}

class StringListConverter {
    @TypeConverter
    fun stringListToString(value: List<String>): String =
        value.joinToString(";")

    @TypeConverter
    fun instantToLong(value: String?): List<String> =
        if (value.isNullOrEmpty()) {
            emptyList()
        } else {
            value.split(";").toList()
        }
}

class LocalDateConverter {
    @TypeConverter
    fun localDateToInt(value: LocalDate?): Int? =
        value?.toEpochDays()
    @TypeConverter
    fun intToLocalDate(value: Int?): LocalDate? =
        value?.let { LocalDate.fromEpochDays(it) }
}