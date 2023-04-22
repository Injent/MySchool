package me.injent.myschool.core.network

import kotlinx.datetime.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class IdSerializer : KSerializer<Long> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("StrIdToLongId", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Long
        = decoder.decodeString().toLong()
    override fun serialize(encoder: Encoder, value: Long) {
        encoder.encodeString(value.toString())
    }
}

class LocalDateWithoutTimeSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor
        get() =  PrimitiveSerialDescriptor("LocalDateWithoutTime", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate) =
        encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder) =
        LocalDate.parse(decoder.decodeString().substring(0, 10)) // get only yyyy-MM-dd
}

class EpochLocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("EpochLocalDate", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): LocalDate {
        return Instant.fromEpochSeconds(decoder.decodeLong()).toLocalDateTime(TimeZone.UTC).date
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeLong(value.atStartOfDayIn(TimeZone.UTC).epochSeconds)
    }
}