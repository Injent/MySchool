package me.injent.myschool.core.model

import kotlinx.datetime.LocalDateTime

data class MarkDetails(
    val date: LocalDateTime,
    val markInfo: MarkInfo,
    val categories: List<Category>
) {
    data class MarkInfo(
        val isImportant: Boolean,
        val isFinal: Boolean,
        val markTypeText: String,
        val marks: List<Mark>,
        val elapsedSetMarkTime: String,
    )

    data class Mark(
        val id: Long,
        val value: String,
        val mood: me.injent.myschool.core.model.Mark.Mood
    )

    data class Category(
        val mood: me.injent.myschool.core.model.Mark.Mood,
        val percent: Float,
        val studentCount: Int,
        val value: String
    )
}
