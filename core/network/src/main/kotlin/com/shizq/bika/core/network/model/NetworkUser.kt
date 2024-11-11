package com.shizq.bika.core.network.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class NetworkUser(
    @SerialName("avatar")
    val avatar: Thumb = Thumb(),
    @SerialName("birthday")
    val birthday: String = "",
    @SerialName("character")
    val character: String = "",
    @SerialName("characters")
    val characters: List<String> = listOf(),
    @SerialName("comicsUploaded")
    val comicsUploaded: Int = 0,
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("exp")
    val exp: Int = 0,
    @Serializable(GenderSerializer::class)
    val gender: String = "",
    @SerialName("_id")
    val id: String = "",
    @SerialName("isPunched")
    val isPunched: Boolean = false,
    @SerialName("level")
    val level: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("role")
    val role: String = "",
    @SerialName("slogan")
    val slogan: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("verified")
    val verified: Boolean = false,
)

internal object GenderSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("GenderSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String {
        return when (decoder.decodeString()) {
            "m" -> "(绅士)"
            "f" -> "(淑女)"
            else -> "(机器人)"
        }
    }

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }
}
