package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkComicInfo(
    @SerialName("comic")
    val comic: Comic = Comic()
) {
    @Serializable
    data class Comic(
        @SerialName("allowComment")
        val allowComment: Boolean = false,
        @SerialName("allowDownload")
        val allowDownload: Boolean = false,
        @SerialName("author")
        val author: String = "",
        @SerialName("categories")
        val categories: List<String> = listOf(),
        @SerialName("chineseTeam")
        val chineseTeam: String = "",
        @SerialName("commentsCount")
        val commentsCount: Int = 0,
        @SerialName("created_at")
        val createdAt: String = "",
        @SerialName("_creator")
        val creator: User = User(),
        @SerialName("description")
        val description: String = "",
        @SerialName("epsCount")
        val epsCount: Int = 0,
        @SerialName("finished")
        val finished: Boolean = false,
        @SerialName("_id")
        val id: String = "",
        @SerialName("isFavourite")
        val isFavourite: Boolean = false,
        @SerialName("isLiked")
        val isLiked: Boolean = false,
        @SerialName("likesCount")
        val likesCount: Int = 0,
        @SerialName("pagesCount")
        val pagesCount: Int = 0,
        @SerialName("tags")
        val tags: List<String> = listOf(),
        @SerialName("thumb")
        val thumb: Thumb = Thumb(),
        @SerialName("title")
        val title: String = "",
        @SerialName("totalLikes")
        val totalLikes: Int = 0,
        @SerialName("totalViews")
        val totalViews: Int = 0,
        @SerialName("updated_at")
        val updatedAt: String = "",
        @SerialName("viewsCount")
        val viewsCount: Int = 0
    )
}
