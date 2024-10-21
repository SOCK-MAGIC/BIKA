package com.shizq.bika.feature.comic.list

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.data.repository.RecentlyViewedComicRepository
import com.shizq.bika.core.datastore.BikaInterestsDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.feature.comic.favourite.FavouritePagingSource
import com.shizq.bika.feature.comic.list.SortDialog.sortFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ComicListComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted category: String?,
    private val network: BikaNetworkDataSource,
    private val userInterests: BikaInterestsDataSource,
    private val recentlyViewedComicRepository: RecentlyViewedComicRepository,
) : ComicListComponent,
    ComponentContext by componentContext {

    override val categoryVisibilityUiState = userInterests.userData.map { it.categoriesVisibility }
        .stateIn(
            componentScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap(),
        )
    override val comicFlow = combine(userInterests.userHideCategories, sortFlow, ::Pair)
        .flatMapLatest { (hide, sort) ->
            when (category) {
                "recommend" -> {
                    Pager(
                        config = PagingConfig(pageSize = 20),
                    ) { ComicRecommendPagingSource(network) }.flow
                }

                "random" -> {
                    Pager(
                        config = PagingConfig(pageSize = 20),
                    ) { ComicRandomPagingSource(network) }.flow
                }

                "recently" -> {
                    recentlyViewedComicRepository.getRecentWatchedComicQueries()
                }
                "favourite" -> {
                    Pager(
                        config = PagingConfig(pageSize = 20),
                    ) { FavouritePagingSource(network, sort) }.flow
                }
                else -> {
                    Pager(
                        config = PagingConfig(pageSize = 20),
                        initialKey = 1,
                    ) { ComicListPagingSource(network, category, sort) }
                        .flow
                        .map { pagingData ->
                            pagingData.filter { comic ->
                                Napier.i(tag = "filter category") { hide.joinToString() }
                                for (c in comic.categories) {
                                    if (hide.contains(c)) {
                                        return@filter false
                                    }
                                }
                                return@filter true
                            }
                        }
                }
            }
        }
        .cachedIn(componentScope)

    override fun updateCategoryState(name: String, state: Boolean) {
        componentScope.launch {
            userInterests.setCategoriesVisibility(name, state)
        }
    }

    @AssistedFactory
    interface Factory : ComicListComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            category: String?,
        ): ComicListComponentImpl
    }
}
