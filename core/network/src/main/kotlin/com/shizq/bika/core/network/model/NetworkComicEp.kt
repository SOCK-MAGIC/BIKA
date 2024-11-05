package com.shizq.bika.core.network.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class NetworkComicEp(
    @SerialName("eps")
    val eps: Eps = Eps(),
) {
    @Serializable
    data class Eps(
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
            @JsonNames("_id", "id")
            val id: String = "",
            @SerialName("order")
            val order: Int = 0,
            @SerialName("title")
            val title: String = "",
            @SerialName("updated_at")
            val updatedAt: String = "",
        )
    }
}


