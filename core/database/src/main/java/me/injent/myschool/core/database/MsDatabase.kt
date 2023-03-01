package me.injent.myschool.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.injent.myschool.core.database.dao.PersonDao
import me.injent.myschool.core.database.model.PersonEntity
import me.injent.myschool.core.database.util.InstantConverter

@Database(
    version = 1,
    entities = [
        PersonEntity::class
    ]
)
@TypeConverters(
    InstantConverter::class
)
abstract class MsDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
}