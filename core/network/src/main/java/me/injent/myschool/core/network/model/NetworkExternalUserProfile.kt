package me.injent.myschool.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import me.injent.myschool.core.model.ExternalUserProfile

@Serializable
data class NetworkExternalUserProfile(
    val personId: Long,
    val shortName: String,
    val locale: String,
    @Serializable(with = InstantSerializer::class)
    val birthday: Instant? = null,
    val sex: String,
    val roles: List<String>,
    val phone: String? = null
)

fun NetworkExternalUserProfile.asExternalModel() = ExternalUserProfile(
    personId = personId,
    shortName = shortName,
    locale = locale,
    birthday = birthday,
    sex = sex,
    roles = roles,
    phone = phone
)

object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor
        = PrimitiveSerialDescriptor("Instant", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Instant)
        = encoder.encodeLong(value.toEpochMilliseconds())
    override fun deserialize(decoder: Decoder)
        = Instant.fromEpochMilliseconds(decoder.decodeLong())
}