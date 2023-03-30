package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.model.Sex
import me.injent.myschool.core.network.LocalDateWithoutTimeSerializer

@Serializable
data class NetworkPerson(
    val id: Long,
    val personId: Long,
    val shortName: String,
    @Serializable(with = LocalDateWithoutTimeSerializer::class)
    val birthday: LocalDate? = null,
    val sex: Sex,
    val roles: List<String>,
    val phone: String? = null
)

fun NetworkPerson.asExternalModel() = Person(
    id = id,
    personId = personId,
    shortName = shortName,
    birthday = birthday,
    sex = sex,
    roles = roles,
    phone = phone
)