package com.shizq.bika.feature.comic.info

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.data.repository.RecentlyViewedComicRepository
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.NetworkComicInfo
import com.shizq.bika.core.network.model.Thumb
import com.shizq.bika.core.result.Result
import com.shizq.bika.core.result.asResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ComicInfoComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted id: String,
    private val network: BikaNetworkDataSource,
    private val recentlyViewedComicRepository: RecentlyViewedComicRepository,
) : ComicInfoComponent,
    ComponentContext by componentContext {

    override val comicInfoUiState = flowOf(id)
        .map { network.getComicInfo(it) }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> ComicInfoUiState.Error
                Result.Loading -> ComicInfoUiState.Loading
                is Result.Success -> {
                    val comic = result.data.comic
                    val creator = comic.creator
                    ComicInfoUiState.Success(
                        ToolItem(
                            allowComment = comic.allowComment,
                            allowDownload = comic.allowDownload,
                            commentsCount = comic.commentsCount,
                            isFavourite = comic.isFavourite,
                            isLiked = comic.isLiked,
                            totalLikes = comic.totalLikes,
                            pagesCount = comic.pagesCount,
                            epsCount = comic.epsCount,
                        ),
                        chineseTeam = comic.chineseTeam,
                        createdAt = comic.createdAt,
                        creator = NetworkComicInfo.Comic.Creator(
                            avatar = Thumb(
                                fileServer = "${creator.avatar.fileServer}/static/${creator.avatar.path}",
                                originalName = "",
                                path = "",
                            ),
                            characters = creator.characters,
                            exp = creator.exp,
                            gender = creator.gender,
                            id = creator.id,
                            level = creator.level,
                            name = creator.name,
                            role = creator.role,
                            slogan = creator.slogan,
                            title = creator.title,
                            verified = creator.verified,
                        ),
                        description = comic.description,
                        tags = comic.tags,
                        totalViews = comic.totalViews,
                        updatedAt = comic.updatedAt,
                        viewsCount = comic.viewsCount,
                        comicResource = ComicResource(
                            comic.id,
                            comic.thumb.imageUrl,
                            comic.title,
                            comic.author,
                            comic.categories,
                            comic.finished,
                            comic.epsCount,
                            comic.pagesCount,
                            comic.likesCount,
                        ),
                    )
                }
            }
        }.stateIn(
            componentScope,
            SharingStarted.WhileSubscribed(5_000),
            ComicInfoUiState.Loading,
        )

    override fun onClickTrigger(comicResource: ComicResource) {
        componentScope.launch {
            recentlyViewedComicRepository.insertOrReplaceRecentWatchedComic(comicResource)
        }
    }

    @AssistedFactory
    interface Factory : ComicInfoComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            id: String,
        ): ComicInfoComponentImpl
    }
}
