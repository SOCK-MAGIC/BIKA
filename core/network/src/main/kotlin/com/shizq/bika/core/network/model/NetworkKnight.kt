package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkKnight(
    val users: List<User>,
) {
    @Serializable
    data class User(
        @SerialName("avatar")
        val avatar: Thumb = Thumb(),
        @SerialName("character")
        val character: String = "",
        @SerialName("characters")
        val characters: List<String> = listOf(),
        @SerialName("comicsUploaded")
        val comicsUploaded: Int = 0,
        @SerialName("exp")
        val exp: Int = 0,
        @SerialName("gender")
        val gender: String = "",
        @SerialName("_id")
        val id: String = "",
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
}
