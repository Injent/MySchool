package me.injent.myschool.core.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonTransformingSerializer
import me.injent.myschool.core.network.model.NetworkEduGroup
import me.injent.myschool.core.network.model.NetworkSchool

object SchoolSerializer : JsonTransformingSerializer<List<NetworkSchool>>
    (ListSerializer(NetworkSchool.serializer()))

object EduGroupSerializer : JsonTransformingSerializer<List<NetworkEduGroup>>
    (ListSerializer(NetworkEduGroup.serializer()))

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