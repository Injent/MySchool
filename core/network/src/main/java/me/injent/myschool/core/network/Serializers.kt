package me.injent.myschool.core.network

import kotlinx.datetime.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import me.injent.myschool.core.network.model.NetworkMark
import me.injent.myschool.core.network.model.NetworkUserFeed

class IdSerializer : KSerializer<Long> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("StrIdToLongId", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Long
        = decoder.decodeString().toLong()
    override fun serialize(encoder: Encoder, value: Long) {
        encoder.encodeString(value.toString())
    }
}

class DaySerializer : KSerializer<NetworkUserFeed.Day> {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("NetworkUserFeed.Day", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): NetworkUserFeed.Day {
        require(decoder is JsonDecoder)
        val day = decoder.decodeJsonElement()

        val marksCards = day.jsonObject["summary"]!!.jsonObject["marksCards"]!!.jsonArray
            .map { card ->
                val subjectName: String =
                    json.decodeFromJsonElement(card.jsonObject["subjectName"]!!.jsonPrimitive)
                val subjectId: Long =
                    json.decodeFromJsonElement(card.jsonObject["subjectId"]!!.jsonPrimitive)
                val workTypeName: String =
                    json.decodeFromJsonElement(card.jsonObject["workTypeName"]!!.jsonPrimitive)
                val marks = card.jsonObject["marks"]!!.jsonArray
                    .map { mark ->
                        json.decodeFromJsonElement<NetworkMark>(mark.jsonObject["mark"]!!)
                    }
                val lesson: NetworkUserFeed.Lesson? = card.jsonObject["lesson"]?.run {
                    json.decodeFromJsonElement(this)
                }

                NetworkUserFeed.MarkCard(
                    subjectName = subjectName,
                    subjectId = subjectId,
                    workTypeName = workTypeName,
                    marks = marks,
                    lesson = lesson
                )
            }

        val nextWorkingDayDate: LocalDateTime? =
            json.decodeFromJsonElement(day.jsonObject["nextWorkingDayDate"]!!)
        val date: LocalDateTime =
            json.decodeFromJsonElement(day.jsonObject["date"]!!)
        val todayHomeworks: List<NetworkUserFeed.Homework> =
            json.decodeFromJsonElement(day.jsonObject["todayHomeworks"]!!)
        val todaySchedule: List<NetworkUserFeed.Schedule> =
            json.decodeFromJsonElement(day.jsonObject["todaySchedule"]!!)

        return NetworkUserFeed.Day(
            date = date,
            nextWorkingDayDate = nextWorkingDayDate,
            marksCards = marksCards,
            todayHomeworks = todayHomeworks,
            todaySchedule = todaySchedule
        )
    }

    override fun serialize(encoder: Encoder, value: NetworkUserFeed.Day) {
        error("NetworkUserFeed.Day is used only for deserialization")
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