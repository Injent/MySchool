package me.injent.myschool.core.model

import android.webkit.MimeTypeMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

data class Attachment(
    val id: Long,
    val type: FileType,
    val name: String,
    val downloadUrl: String,
    val size: Long
) {
    val extension: String
        get() = MimeTypeMap.getFileExtensionFromUrl(downloadUrl)
}

@Serializable(FileTypeSerializer::class)
enum class FileType(val extensions: String? = null) {
    PDF,
    TXT,
    OTHER
}

class FileTypeSerializer : KSerializer<FileType> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("FileType", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): FileType {
        return try {
            FileType.valueOf(decoder.decodeString().uppercase())
        } catch (_: Exception) {
            FileType.OTHER
        }
    }

    override fun serialize(encoder: Encoder, value: FileType) {
        encoder.encodeString(value.name)
    }
}