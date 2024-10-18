package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRankingDetail(
    val comics: List<Comics>,
) {
    @Serializable
    data class Comics(
        @SerialName("_id")
        val id: String = "",
        val title: String = "",
        val author: String = "",
        val pagesCount: Int = 0,
        val epsCount: Int = 0,
        val finished: Boolean = false,
        val thumb: Thumb = Thumb(),
        val totalViews: Int = 0,
        val totalLikes: Int = 0,
        val likesCount: Int = 0,
        val viewsCount: Int = 0,
        val leaderboardCount: Int = 0,
        val categories: List<String> = listOf(),
    )
}
