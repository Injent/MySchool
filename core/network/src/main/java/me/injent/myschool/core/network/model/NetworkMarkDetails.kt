package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.EpochLocalDateTimeSerializer
import me.injent.myschool.core.model.MarkDetails

@Serializable
data class NetworkMarkDetails(
    @Serializable(EpochLocalDateTimeSerializer::class)
    val date: LocalDateTime?,
    val markDetails: MarkDetails,
    val categories: List<Category>
) {
    @Serializable
    data class MarkDetails(
        val isImportant: Boolean,
        val isFinal: Boolean,
        val markTypeText: String,
        val marks: List<Mark>,
        val elapsedSetMark: ElapsedSetMark,
    )

    @Serializable
    data class ElapsedSetMark(
        val elapsedSetMarkTime: String
    )

    @Serializable
    data class Mark(
        val id: Long,
        val value: String,
        val mood: me.injent.myschool.core.model.Mark.Mood
    )

    @Serializable
    data class Category(
        val mood: me.injent.myschool.core.model.Mark.Mood,
        val percent: Float,
        val studentCount: Int,
        val value: String
    ) {
        fun asExternalModel() = me.injent.myschool.core.model.MarkDetails.Category(
            mood = mood,
            percent = percent,
            studentCount = studentCount,
            value = value
        )
    }
}

fun NetworkMarkDetails.asExternalModel() = MarkDetails(
    date = date,
    markInfo = with(markDetails) {
        MarkDetails.MarkInfo(
            isImportant = isImportant,
            isFinal = isFinal,
            markTypeText = markTypeText,
            marks = marks.map { MarkDetails.Mark(it.id, it.value, it.mood) },
            elapsedSetMarkTime = elapsedSetMark.elapsedSetMarkTime
        )
    },
    categories = categories.map(NetworkMarkDetails.Category::asExternalModel)
)