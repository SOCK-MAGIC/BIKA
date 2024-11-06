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
        val docs: List<NetworkComicSimple> = listOf(),
        @SerialName("limit")
        val limit: Int = 0,
        @SerialName("page")
        val page: Int = 0,
        @SerialName("pages")
        val pages: Int = 0,
        @SerialName("total")
        val total: Int = 0,
    )
}
