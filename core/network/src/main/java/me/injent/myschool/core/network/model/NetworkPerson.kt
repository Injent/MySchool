package me.injent.myschool.core.network.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import me.injent.myschool.core.model.Person

@Serializable
data class NetworkPerson(
    val id: Long,
    val shortName: String,
    val locale: String,
    @Serializable(with = CustomLocalDateSerializer::class)
    val birthday: LocalDate? = null,
    val sex: String,
    val roles: List<String>,
    val phone: String? = null
)

fun NetworkPerson.asExternalModel() = Person(
    id = id,
    shortName = shortName,
    locale = locale,
    birthday = birthday,
    sex = sex,
    roles = roles,
    phone = phone
)

object CustomLocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor
        = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate)
        = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder)
        = LocalDate.parse(decoder.decodeString().substring(0, 10)) // get only yyyy-MM-dd
}