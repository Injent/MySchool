package me.injent.myschool.core.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Uses for viewing other user profile page
 */
data class Person(
    val id: Long,
    val personId: Long,
    val shortName: String,
    val birthday: LocalDate? = null,
    val sex: Sex,
    val roles: List<String>,
    val phone: String? = null
)

@Serializable(SexSerializer::class)
enum class Sex(val serializeName: String) {
    Male("Male"),
    Female("Female")
}

class SexSerializer : KSerializer<Sex> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Sex", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Sex) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): Sex {
        return Sex.valueOf(decoder.decodeString())
    }
}