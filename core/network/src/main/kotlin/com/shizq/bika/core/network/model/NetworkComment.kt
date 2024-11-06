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
    val topComments: List<CommentDoc> = listOf(),
) {
    @Serializable
    data class Comments(
        @SerialName("docs")
        val docs: List<CommentDoc> = listOf(),
        @SerialName("limit")
        val limit: Int = 0,
        @SerialName("page")
        val page: String = "",
        @SerialName("pages")
        val pages: Int = 0,
        @SerialName("total")
        val total: Int = 0,
    )
}

@Serializable
data class ChildComment(
    val comments: Comments = Comments(),
) {
    @Serializable
    data class Comments(
        @SerialName("docs")
        val docs: List<CommentDoc> = listOf(),
        @SerialName("limit")
        val limit: Int = 0,
        @SerialName("page")
        val page: String = "",
        @SerialName("pages")
        val pages: Int = 0,
        @SerialName("total")
        val total: Int = 0,
    )
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class CommentDoc(
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
    @SerialName("_parent")
    val parent: String = "",
    @SerialName("totalComments")
    val totalComments: Int = 0,
    @SerialName("_user")
    val netWorkUser: NetworkUser = NetworkUser(),
)
