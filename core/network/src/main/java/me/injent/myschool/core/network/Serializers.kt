package me.injent.myschool.core.network

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

class MarkSerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Mark", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Int
            = decoder.decodeString().toInt()
    override fun serialize(encoder: Encoder, value: Int) {
        encoder.encodeString(value.toString())
    }
}