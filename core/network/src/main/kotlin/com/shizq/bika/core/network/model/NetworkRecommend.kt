package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFirstRecommend(
    @SerialName("collections")
    val collections: List<Collection> = listOf(),
) {
    @Serializable
    data class Collection(
        @SerialName("comics")
        val comics: List<NetworkComicSimple> = listOf(),
    )
}

@Serializable
data class NetworkComicRecommend(
    @SerialName("comics")
    val comics: List<NetworkComicSimple> = listOf(),
)
