package me.injent.myschool.core.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.*
import me.injent.myschool.core.model.Sex

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

class LocalDateTimeConverter {
    @TypeConverter
    fun localDateTimeToLong(value: LocalDateTime?): Long? =
        value?.toInstant(TimeZone.currentSystemDefault())?.epochSeconds
    @TypeConverter
    fun intToLocalDateTime(value: Long?): LocalDateTime? =
        value?.let { Instant.fromEpochSeconds(value).toLocalDateTime(TimeZone.currentSystemDefault()) }
}

class SexConverter {
    @TypeConverter
    fun sexToOrdinal(value: Sex): Int = value.ordinal
    @TypeConverter
    fun ordinalToSex(value: Int): Sex = Sex.values()[value]
}