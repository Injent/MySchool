package me.injent.myschool.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.injent.myschool.core.database.dao.MarkDao
import me.injent.myschool.core.database.dao.PersonDao
import me.injent.myschool.core.database.dao.SubjectDao
import me.injent.myschool.core.database.model.MarkEntity
import me.injent.myschool.core.database.model.PersonEntity
import me.injent.myschool.core.database.model.SubjectEntity
import me.injent.myschool.core.database.util.InstantConverter
import me.injent.myschool.core.database.util.LocalDateConverter
import me.injent.myschool.core.database.util.LocalDateTimeConverter
import me.injent.myschool.core.database.util.StringListConverter

@Database(
    version = 1,
    entities = [
        PersonEntity::class,
        SubjectEntity::class,
        MarkEntity::class
    ]
)
@TypeConverters(
    InstantConverter::class,
    StringListConverter::class,
    LocalDateConverter::class,
    LocalDateTimeConverter::class
)
abstract class MsDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun subjectDao(): SubjectDao
    abstract fun markDao(): MarkDao
}