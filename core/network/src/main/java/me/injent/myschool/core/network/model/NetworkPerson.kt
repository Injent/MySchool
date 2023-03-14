package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.model.Sex
import me.injent.myschool.core.network.IdSerializer

@Serializable
data class NetworkPerson(
    val id: Long,
    @SerialName("personId_str")
    @Serializable(IdSerializer::class)
    val personId: Long,
    val shortName: String,
    val locale: String,
    @Serializable(with = CustomLocalDateSerializer::class)
    val birthday: LocalDate? = null,
    @Serializable(SexSerializer::class)
    val sex: Sex,
    val roles: List<String>,
    val phone: String? = null
)

fun NetworkPerson.asExternalModel() = Person(
    id = id,
    personId = personId,
    shortName = shortName,
    locale = locale,
    birthday = birthday,
    sex = sex,
    roles = roles,
    phone = phone
)

class CustomLocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor
        = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate)
        = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder)
        = LocalDate.parse(decoder.decodeString().substring(0, 10)) // get only yyyy-MM-dd
}

class SexSerializer : KSerializer<Sex> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Sex", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Sex) {
        encoder.encodeString(value.serializeName)
    }

    override fun deserialize(decoder: Decoder): Sex {
        return if (decoder.decodeString() == "Male") {
            Sex.MALE
        } else {
            Sex.FEMALE
        }
    }
}