package me.injent.myschool.core.analytics

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    abstract fun bindsAnalyticsLogger(analyticsLogger: DebugAnalyticsLogger): AnalyticsLogger
}