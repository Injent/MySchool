package me.injent.myschool.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.core.database.DatabaseMigrations
import me.injent.myschool.core.database.MsDatabase
import me.injent.myschool.core.database.dao.MarkDao
import me.injent.myschool.core.database.dao.PersonDao
import me.injent.myschool.core.database.dao.SubjectDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesMsDatabase(
        @ApplicationContext context: Context
    ): MsDatabase = Room.databaseBuilder(
        context,
        MsDatabase::class.java,
        "ms-database"
    )
        .addMigrations(*DatabaseMigrations.migrations)
        .build()

    @Provides
    fun providesPersonDao(
        database: MsDatabase
    ): PersonDao = database.personDao()

    @Provides
    fun providesSubjectDao(
        database: MsDatabase
    ): SubjectDao = database.subjectDao()

    @Provides
    fun providesMarkDao(
        database: MsDatabase
    ): MarkDao = database.markDao()
}