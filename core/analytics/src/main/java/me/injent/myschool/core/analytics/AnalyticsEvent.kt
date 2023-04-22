package me.injent.myschool.core.analytics

data class AnalyticsEvent(
    val type: String,
    val extras: List<Param> = emptyList()
) {
    companion object {
        const val SCREEN_VIEW = "screen_view"
        const val BACKGROUND_WORK = "background_work"
    }

    data class Param(val key: String, val value: String) {
        companion object {
            const val SCREEN_NAME = "screen_name"
        }
    }
}
