package com.shizq.bika.feature.comic.info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shizq.bika.core.data.repository.RecentlyViewedComicRepository
import com.shizq.bika.core.data.util.asComicResource
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.result.Result
import com.shizq.bika.core.result.asResult
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class ComicInfoViewModel @AssistedInject constructor(
    private val comicId: String,
    private val network: BikaNetworkDataSource,
    private val recentlyViewedComicRepository: RecentlyViewedComicRepository,
) : ViewModel() {
    private var hasClick by mutableStateOf(false)

    val comicInfoUiState = snapshotFlow { hasClick to comicId }
        .map { it.second }
        .transform {
            coroutineScope {
                val args1 = async { network.getComicInfo(it) }
                val args3 = async { network.getRecommend(it) }
                emit(Pair(args1.await(), args3.await()))
            }
        }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> ComicInfoUiState.Error
                Result.Loading -> ComicInfoUiState.Loading
                is Result.Success -> {
                    val (info, recommend) = result.data
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
                        bottomRecommend = recommend.comics.map { it.asComicResource() },
                    )
                }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ComicInfoUiState.Loading,
        )
    val epUiState = flow { emit(network.getComicAllEp(comicId)) }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> EpUiState.Error
                Result.Loading -> EpUiState.Loading
                is Result.Success -> EpUiState.Success(
                    result.data.chunked(4) { docs -> docs.map { it.asEpDoc() } },
                )
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            EpUiState.Loading,
        )

    fun onClickTrigger(comicResource: ComicResource) {
        viewModelScope.launch {
            recentlyViewedComicRepository.insertOrReplaceRecentWatchedComic(comicResource)
        }
    }

    fun onSwitchLike() {
        viewModelScope.launch {
            network.switchLike(comicId)
            hasClick = !hasClick
        }
    }

    fun onSwitchFavorite() {
        viewModelScope.launch {
            network.switchFavourite(comicId)
            hasClick = !hasClick
        }
    }
}
