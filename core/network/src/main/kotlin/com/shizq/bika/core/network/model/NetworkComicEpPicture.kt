package com.shizq.bika.core.network.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class NetworkComicEpPicture(
    @SerialName("ep")
    val ep: Ep = Ep(),
    @SerialName("pages")
    val pages: Pages = Pages(),
) {
    @Serializable
    data class Ep(
        @SerialName("_id")
        val id: String = "",
        @SerialName("title")
        val title: String = "",
    )

    @Serializable
    data class Pages(
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
        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        data class Doc(
            @JsonNames("id", "_id")
            val id: String = "",
            @SerialName("media")
            val media: Thumb = Thumb(),
        )
    }
}
