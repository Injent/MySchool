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
import me.injent.myschool.core.database.util.*

@Database(
    version = DatabaseMigrations.DATABASE_VERSION,
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
    LocalDateTimeConverter::class,
    SexConverter::class,
    MarkMoodConverter::class
)
abstract class MsDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun subjectDao(): SubjectDao
    abstract fun markDao(): MarkDao
}