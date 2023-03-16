package me.injent.myschool.core.model

import kotlinx.datetime.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class Period(
    val id: Long,
    val isCurrent: Boolean,
    @Serializable(LongToLocalDateTimeSerializer::class)
    val dateFinish: LocalDateTime,
    @Serializable(LongToLocalDateTimeSerializer::class)
    val dateStart: LocalDateTime,
    val number: Int,
    val studyYear: Int,
    val type: PeriodType
)

class LongToLocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LongToLocalDateTime", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return Instant.fromEpochSeconds(decoder.decodeLong()).toLocalDateTime(TimeZone.UTC)
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeLong(value.toInstant(TimeZone.UTC).epochSeconds)
    }
}
