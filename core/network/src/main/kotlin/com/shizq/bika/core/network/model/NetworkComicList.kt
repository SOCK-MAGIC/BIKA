package com.shizq.bika.core.network.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class NetworkComicList(
    @SerialName("comics")
    val comics: Comics = Comics()
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
        val total: Int = 0
    ) {
        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        data class Doc(
            @SerialName("author")
            val author: String = "",
            @SerialName("categories")
            val categories: List<String> = listOf(),
            @SerialName("epsCount")
            val epsCount: Int = 0,
            @SerialName("finished")
            val finished: Boolean = false,
            @JsonNames("_id", "id")
            val id: String = "",
            @SerialName("likesCount")
            val likesCount: Int = 0,
            @SerialName("pagesCount")
            val pagesCount: Int = 0,
            @SerialName("thumb")
            val thumb: Thumb = Thumb(),
            @SerialName("title")
            val title: String = "",
            @SerialName("totalLikes")
            val totalLikes: Int = 0,
            @SerialName("totalViews")
            val totalViews: Int = 0
        )
    }
}
