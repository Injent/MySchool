package me.injent.myschool.core.analytics

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

const val TAG = "DebugAnalyticsLogger"

@Singleton
class DebugAnalyticsLogger @Inject constructor() : AnalyticsLogger {
    override fun logEvent(event: AnalyticsEvent) {
        Log.d(TAG, "${event.type}: ${event.extras.joinToString(";")}")
    }
}