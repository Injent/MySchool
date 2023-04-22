package me.injent.myschool.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Declaration of database migrations which will be added to
 * database builder automaticly
 */
object DatabaseMigrations {
    const val DATABASE_VERSION = 2

    val migrations = arrayOf<Migration>(
        object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE persons ADD avatar_url TEXT NULLABLE")
            }
        }
    )
}