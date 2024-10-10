package com.shizq.bika.feature.comic.info

import com.shizq.bika.core.network.model.NetworkComicInfo.Comic.Creator

sealed interface ComicInfoUiState {
    data object Loading : ComicInfoUiState
    data object Error : ComicInfoUiState
    data class Success(
        val allowComment: Boolean = false,
        val allowDownload: Boolean = false,
        val author: String = "",
        val categories: List<String> = listOf(),
        val chineseTeam: String = "",
        val commentsCount: Int = 0,
        val createdAt: String = "",
        val creator: Creator = Creator(),
        val description: String = "",
        val epsCount: Int = 0,
        val finished: Boolean = false,
        val id: String = "",
        val isFavourite: Boolean = false,
        val isLiked: Boolean = false,
        val likesCount: Int = 0,
        val pagesCount: Int = 0,
        val tags: List<String> = listOf(),
        val coverUrl: String,
        val title: String = "",
        val totalLikes: Int = 0,
        val totalViews: Int = 0,
        val updatedAt: String = "",
        val viewsCount: Int = 0,
    ) : ComicInfoUiState
}
