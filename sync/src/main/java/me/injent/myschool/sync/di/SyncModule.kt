package me.injent.myschool.sync.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.core.data.util.SyncStatusMonitor
import me.injent.myschool.sync.monitor.SyncWorkStatusMonitor

@Module
@InstallIn(SingletonComponent::class)
interface SyncModule {
    @Binds
    fun bindsSyncStatusMonitor(
        monitor: SyncWorkStatusMonitor
    ): SyncStatusMonitor
}