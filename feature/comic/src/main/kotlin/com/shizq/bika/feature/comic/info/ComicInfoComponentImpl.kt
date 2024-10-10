package com.shizq.bika.feature.comic.info

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
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
import kotlinx.serialization.Serializable

class ComicInfoComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted id: String,
    private val network: BikaNetworkDataSource,
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
                        allowComment = comic.allowComment,
                        allowDownload = comic.allowDownload,
                        author = comic.author,
                        categories = comic.categories,
                        chineseTeam = comic.chineseTeam,
                        commentsCount = comic.commentsCount,
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
                        epsCount = comic.epsCount,
                        finished = comic.finished,
                        id = comic.id,
                        isFavourite = comic.isFavourite,
                        isLiked = comic.isLiked,
                        likesCount = comic.likesCount,
                        pagesCount = comic.pagesCount,
                        tags = comic.tags,
                        coverUrl = comic.thumb.imageUrl,
                        title = comic.title,
                        totalLikes = comic.totalLikes,
                        totalViews = comic.totalViews,
                        updatedAt = comic.updatedAt,
                        viewsCount = comic.viewsCount,
                    )
                }
            }
        }.stateIn(
            componentScope,
            SharingStarted.WhileSubscribed(5_000),
            ComicInfoUiState.Loading,
        )

    @AssistedFactory
    interface Factory : ComicInfoComponent.Factory {
        override fun invoke(
            componentContext: @Serializable
            ComponentContext,
            id: String,
        ): ComicInfoComponentImpl
    }
}
