package me.injent.myschool.core.analytics

interface AnalyticsLogger {
    fun logEvent(event: AnalyticsEvent)
}