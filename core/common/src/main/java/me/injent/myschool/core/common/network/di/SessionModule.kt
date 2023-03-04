package me.injent.myschool.core.common.network.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.core.common.SessionManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {
    @Provides
    @Singleton
    fun providesSessionManager(
        @ApplicationContext context: Context
    ) = SessionManager(context)
}