package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComicInSearch(
    val comics: Comics = Comics(),
) {
    @Serializable
    data class Comics(
        @SerialName("docs")
        val docs: List<Doc> = listOf(),
        @SerialName("limit")
        val limit: Int = 0,
        @SerialName("page")
        val page: Int = 0,
        @SerialName("pages")
        val pages: Int = 0,
        @SerialName("total")
        val total: Int = 0,
    ) {
        @Serializable
        data class Doc(
            @SerialName("author")
            val author: String = "",
            @SerialName("categories")
            val categories: List<String> = listOf(),
            @SerialName("chineseTeam")
            val chineseTeam: String = "",
            @SerialName("created_at")
            val createdAt: String = "",
            @SerialName("description")
            val description: String = "",
            @SerialName("finished")
            val finished: Boolean = false,
            @SerialName("_id")
            val id: String = "",
            @SerialName("likesCount")
            val likesCount: Int = 0,
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
        )
    }
}
