package com.shizq.bika.feature.comic.info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class ComicInfoComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val comicId: String,
    private val network: BikaNetworkDataSource,
    private val recentlyViewedComicRepository: RecentlyViewedComicRepository,
) : ComicInfoComponent,
    ComponentContext by componentContext {
    private var hasClick by mutableStateOf(false)

    init {
        componentScope.launch {
            network.getComicAllEp(comicId, 1)

        }
    }

    val comicInfoUiState2 = snapshotFlow { hasClick to comicId }
        .map { it.second }
        .transform {
            val args1 = flow { emit(network.getComicInfo(it)) }
            val args2 = flow { emit(network.getComicAllEp(it, 1)) }
            val args3 = flow { emit(network.getRecommend(it)) }
            emit(Triple(args1, args2, args3))
        }.asResult()
        .flatMapConcat { result ->
            when (result) {
                is Result.Error -> TODO()
                Result.Loading -> TODO()
                is Result.Success -> {


                }
            }
            flow { emit(Unit) }
        }
    override val comicInfoUiState = snapshotFlow { hasClick to comicId }
        .map { it.second }
        .transform {
            val args1 = network.getComicInfo(it)
            val args2 = network.getComicAllEp(it, 1)
            val args3 = network.getRecommend(it)
            emit(Triple(args1, args2, args3))
        }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> ComicInfoUiState.Error
                Result.Loading -> ComicInfoUiState.Loading
                is Result.Success -> {
                    val (info, eps, recommend) = result.data
                    val comic = info.comic
                    ComicInfoUiState.Success(
                        info.asToolItems(),
                        chineseTeam = comic.chineseTeam,
                        createdAt = comic.createdAt,
                        creator = info.asCreator(),
                        description = comic.description,
                        tags = comic.tags,
                        totalViews = comic.totalViews,
                        updatedAt = comic.updatedAt,
                        viewsCount = comic.viewsCount,
                        comicResource = info.asComicResource(),
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

    override fun onSwitchLike() {
        componentScope.launch {
            network.switchLike(comicId)
            hasClick = !hasClick
        }
    }

    override fun onSwitchFavorite() {
        componentScope.launch {
            network.switchFavourite(comicId)
            hasClick = !hasClick
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

fun NetworkComicInfo.asToolItems(): ToolItem {
    return ToolItem(
        allowComment = comic.allowComment,
        allowDownload = comic.allowDownload,
        commentsCount = comic.commentsCount,
        isFavourite = comic.isFavourite,
        isLiked = comic.isLiked,
        totalLikes = comic.totalLikes,
        pagesCount = comic.pagesCount,
        epsCount = comic.epsCount,
    )
}

fun NetworkComicInfo.asComicResource(): ComicResource {
    return ComicResource(
        comic.id,
        comic.thumb.imageUrl,
        comic.title,
        comic.author,
        comic.categories,
        comic.finished,
        comic.epsCount,
        comic.pagesCount,
        comic.likesCount,
    )
}

fun NetworkComicInfo.asCreator(): Creator {
    val creator = comic.creator
    return Creator(
        avatarUrl = "${creator.avatar.fileServer}/static/${creator.avatar.path}",
        characters = creator.characters,
        gender = when (creator.gender) {
            "m" -> "(绅士)"
            "f" -> "(淑女)"
            else -> "(机器人)"
        },
        level = creator.level,
        name = creator.name,
        slogan = creator.slogan,
        title = creator.title,
    )
}