package me.injent.myschool.core.model.datastore

import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class LocalDateWithoutTimeSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor
        get() =  PrimitiveSerialDescriptor("LocalDateWithoutTime", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate) =
        encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder) =
        LocalDate.parse(decoder.decodeString().substring(0, 10)) // get only yyyy-MM-dd
}