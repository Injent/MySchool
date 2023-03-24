package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.EpochToLocalDateTimeSerializer
import me.injent.myschool.core.model.MarkDetails

@Serializable
data class MarkDetailsResponse(
    @Serializable(EpochToLocalDateTimeSerializer::class)
    val date: LocalDateTime,
    val markDetails: MarkDetails,
    val categories: List<Category>
) {
    @Serializable
    data class MarkDetails(
        val isImportant: Boolean,
        val isFinal: Boolean,
        val markTypeText: String,
        val marks: List<Mark>,
        val elapsedSetMarkTime: String,
    )

    @Serializable
    data class Mark(
        val id: Long,
        val value: String
    )

    @Serializable
    data class Category(
        val percent: Float,
        val studentCount: Int,
        val value: String
    )
}

fun MarkDetailsResponse.asExternalModel() = MarkDetails(
    date = date,
    markInfo = with(markDetails) {
        MarkDetails.MarkInfo(
            isImportant = isImportant,
            isFinal = isFinal,
            markTypeText = markTypeText,
            marks = marks.map { MarkDetails.Mark(it.id, it.value) },
            elapsedSetMarkTime = elapsedSetMarkTime
        )
    },
    categories = categories.map { MarkDetails.Category(it.percent, it.studentCount, it.value) }
)