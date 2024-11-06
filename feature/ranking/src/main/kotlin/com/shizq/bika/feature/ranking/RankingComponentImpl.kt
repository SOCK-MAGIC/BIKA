package com.shizq.bika.feature.ranking

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.NetworkRankingDetail
import com.shizq.bika.core.network.model.NetworkUser
import com.shizq.bika.core.result.Result
import com.shizq.bika.core.result.asResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RankingComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
) : RankingComponent,
    ComponentContext by componentContext {
    override val rankingUiState = combine(
        flow { emit(network.getRankingDetail("H24")) },
        flow { emit(network.getRankingDetail("D7")) },
        flow { emit(network.getRankingDetail("D30")) },
        flow { emit(network.getKnightRankingDetail()) },
    ) { f1, f2, f3, f4 ->
        Pair(f1 to f2, f3 to f4)
    }.asResult()
        .map { result ->
            when (result) {
                is Result.Error -> RankUiState.Failed
                Result.Loading -> RankUiState.Loading
                is Result.Success -> {
                    val (p1, p2) = result.data
                    RankUiState.Success(
                        p1.first.asComicResource(),
                        p1.second.asComicResource(),
                        p2.first.asComicResource(),
                        p2.second.networkUsers,
                    )
                }
            }
        }.stateIn(
            componentScope,
            SharingStarted.WhileSubscribed(5000),
            RankUiState.Loading,
        )

    init {
        componentScope.launch {
            network.getKnightRankingDetail()
        }
    }

    @AssistedFactory
    interface Factory : RankingComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): RankingComponentImpl
    }
}

sealed interface RankUiState {
    data object Failed : RankUiState
    data object Loading : RankUiState
    data class Success(
        val h24: List<ComicResource>,
        val d7: List<ComicResource>,
        val d30: List<ComicResource>,
        val second: List<NetworkUser>,
    ) : RankUiState
}

private fun NetworkRankingDetail.asComicResource() = comics.map {
    ComicResource(
        id = it.id,
        imageUrl = it.thumb.imageUrl,
        title = it.title,
        author = it.author,
        categories = it.categories,
        finished = it.finished,
        epsCount = it.epsCount,
        pagesCount = it.pagesCount,
        likeCount = it.likesCount,
    )
}
