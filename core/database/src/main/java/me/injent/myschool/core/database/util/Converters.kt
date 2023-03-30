package me.injent.myschool.core.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.*
import me.injent.myschool.core.model.Mark
import me.injent.myschool.core.model.Sex

internal class MarkMoodConverter {
    @TypeConverter
    fun markMoodToOrdinal(value: Mark.Mood): Int = value.ordinal
    @TypeConverter
    fun ordinalToMarkMood(value: Int): Mark.Mood = Mark.Mood.values()[value]
}

internal class InstantConverter {
    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? =
        instant?.toEpochMilliseconds()
}

internal class StringListConverter {
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

internal class LocalDateConverter {
    @TypeConverter
    fun localDateToInt(value: LocalDate?): Int? =
        value?.toEpochDays()
    @TypeConverter
    fun intToLocalDate(value: Int?): LocalDate? =
        value?.let { LocalDate.fromEpochDays(it) }
}

internal class LocalDateTimeConverter {
    @TypeConverter
    fun localDateTimeToLong(value: LocalDateTime?): Long? =
        value?.toInstant(TimeZone.currentSystemDefault())?.epochSeconds
    @TypeConverter
    fun intToLocalDateTime(value: Long?): LocalDateTime? =
        value?.let { Instant.fromEpochSeconds(value).toLocalDateTime(TimeZone.currentSystemDefault()) }
}

internal class SexConverter {
    @TypeConverter
    fun sexToOrdinal(value: Sex): Int = value.ordinal
    @TypeConverter
    fun ordinalToSex(value: Int): Sex = Sex.values()[value]
}