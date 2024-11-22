package com.shizq.bika.feature.comic.info

import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.ui.UserInfo

sealed interface ComicInfoUiState {
    data object Loading : ComicInfoUiState
    data object Error : ComicInfoUiState
    data class Success(
        val toolItem: ToolItem,
        val chineseTeam: String = "",
        val createdAt: String = "",
        val creator: UserInfo,
        val description: String = "",
        val tags: List<String> = listOf(),
        val totalViews: Int = 0,
        val updatedAt: String = "",
        val viewsCount: Int = 0,
        val comicResource: ComicResource,
        val bottomRecommend: List<ComicResource>,
    ) : ComicInfoUiState
}

sealed interface EpUiState {
    data object Loading : EpUiState
    data object Error : EpUiState
    data class Success(val eps: List<List<EpDoc>>) : EpUiState
}

data class ToolItem(
    val allowComment: Boolean,
    val allowDownload: Boolean,
    val commentsCount: Int,
    val isFavourite: Boolean,
    val totalLikes: Int,
    val isLiked: Boolean,
    val pagesCount: Int,
    val epsCount: Int,
)
