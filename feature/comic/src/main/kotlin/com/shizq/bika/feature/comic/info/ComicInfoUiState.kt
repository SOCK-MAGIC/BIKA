package com.shizq.bika.feature.comic.info

import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.model.NetworkComicEp

sealed interface ComicInfoUiState {
    data object Loading : ComicInfoUiState
    data object Error : ComicInfoUiState
    data class Success(
        val toolItem: ToolItem,
        val chineseTeam: String = "",
        val createdAt: String = "",
        val creator: Creator,
        val description: String = "",
        val tags: List<String> = listOf(),
        val totalViews: Int = 0,
        val updatedAt: String = "",
        val viewsCount: Int = 0,
        val comicResource: ComicResource,
        val eps: List<List<NetworkComicEp.Eps.Doc>>,
    ) : ComicInfoUiState
}

data class Creator(
    val avatarUrl: String,
    val characters: List<String> = listOf(),
    val gender: String = "",
    val level: Int = 0,
    val name: String = "",
    val slogan: String = "",
    val title: String = "",
    val id: String,
)

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
