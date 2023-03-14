package me.injent.myschool.core.datastore

import androidx.datastore.core.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import me.injent.myschool.core.datastore.model.SaveableUserData
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object UserDataSerializer : Serializer<SaveableUserData> {
    override val defaultValue: SaveableUserData
        get() = SaveableUserData()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): SaveableUserData {
        return try {
            Json.decodeFromStream(
                deserializer = SaveableUserData.serializer(),
                stream = input
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: SaveableUserData, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = SaveableUserData.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}