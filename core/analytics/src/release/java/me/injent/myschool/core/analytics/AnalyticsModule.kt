package me.injent.myschool.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.core.analytics.AnalyticsLogger
import me.injent.myschool.core.analytics.FirebaseAnalyticsLogger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    abstract fun bindsAnalyticsLogger(analyticsLogger: FirebaseAnalyticsLogger): AnalyticsLogger

    companion object {
        @Provides
        @Singleton
        fun providesFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics
    }
}