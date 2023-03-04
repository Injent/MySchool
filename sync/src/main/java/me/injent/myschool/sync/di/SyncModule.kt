package me.injent.myschool.sync.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.core.data.util.SyncStatusMonitor
import me.injent.myschool.sync.WorkController
import me.injent.myschool.sync.monitor.SyncWorkStatusMonitor

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {
    @Provides
    fun providesWorkController(
        @ApplicationContext context: Context
    ): WorkController = WorkController(WorkManager.getInstance(context))

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {
        @Binds
        fun bindsSyncStatusMonitor(
            monitor: SyncWorkStatusMonitor
        ): SyncStatusMonitor
    }
}