package com.shizq.bika.core.network.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class NetworkComicSimple(
    @SerialName("author")
    val author: String = "",
    @SerialName("categories")
    val categories: List<String> = listOf(),
    @SerialName("epsCount")
    val epsCount: Int = 0,
    @SerialName("finished")
    val finished: Boolean = false,
    @JsonNames("id", "_id")
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
    val totalViews: Int = 0,
)
