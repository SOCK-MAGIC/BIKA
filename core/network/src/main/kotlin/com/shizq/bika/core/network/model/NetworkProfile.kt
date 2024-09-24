package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProfile(
    @SerialName("user")
    val user: User = User()
) {
    @Serializable
    data class User(
        val avatar: Avatar = Avatar(),
        @SerialName("birthday")
        val birthday: String = "",
        @SerialName("character")
        val character: String = "",
        @SerialName("characters")
        val characters: List<String> = listOf(),
        @SerialName("created_at")
        val createdAt: String = "",
        @SerialName("email")
        val email: String = "",
        @SerialName("exp")
        val exp: Int = 0,
        @SerialName("gender")
        val gender: String = "",
        @SerialName("_id")
        val id: String = "",
        @SerialName("isPunched")
        val isPunched: Boolean = false,
        @SerialName("level")
        val level: Int = 0,
        @SerialName("name")
        val name: String = "",
        @SerialName("title")
        val title: String = "",
        @SerialName("verified")
        val verified: Boolean = false
    )

    @Serializable
    data class Avatar(
        val fileServer: String = "",
        val originalName: String = "",
        val path: String = ""
    )
}
