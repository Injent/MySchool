package me.injent.myschool.core.database.util

import kotlinx.datetime.Instant

class InstantConverter {
    @androidx.room.TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @androidx.room.TypeConverter
    fun instantToLong(instant: Instant?): Long? =
        instant?.toEpochMilliseconds()
}