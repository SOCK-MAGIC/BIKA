package com.shizq.bika.core.network.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class NetworkComment(
    @SerialName("comments")
    val comments: Comments = Comments(),
    @SerialName("topComments")
    val topComments: List<Comments.Doc> = listOf(),
) {
    @Serializable
    data class Comments(
        @SerialName("docs")
        val docs: List<Doc> = listOf(),
        @SerialName("limit")
        val limit: Int = 0,
        @SerialName("page")
        val page: String = "",
        @SerialName("pages")
        val pages: Int = 0,
        @SerialName("total")
        val total: Int = 0,
    ) {
        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        data class Doc(
            @SerialName("_comic")
            val comic: String = "",
            @SerialName("commentsCount")
            val commentsCount: Int = 0,
            @SerialName("content")
            val content: String = "",
            @SerialName("created_at")
            val createdAt: String = "",
            @SerialName("hide")
            val hide: Boolean = false,
            @JsonNames("id", "_id")
            val id: String = "",
            @SerialName("isLiked")
            val isLiked: Boolean = false,
            @SerialName("isTop")
            val isTop: Boolean = false,
            @SerialName("likesCount")
            val likesCount: Int = 0,
            @SerialName("totalComments")
            val totalComments: Int = 0,
            @SerialName("_user")
            val user: User = User(),
        ) {
            @Serializable
            data class User(
                @SerialName("avatar")
                val avatar: Thumb = Thumb(),
                @SerialName("character")
                val character: String = "",
                @SerialName("characters")
                val characters: List<String> = listOf(),
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
    }
}
