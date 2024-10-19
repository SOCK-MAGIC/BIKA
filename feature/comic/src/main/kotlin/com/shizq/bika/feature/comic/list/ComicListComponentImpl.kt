package com.shizq.bika.feature.comic.list

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.feature.comic.list.SealTag.hideCategoriesFlow
import com.shizq.bika.feature.comic.list.SortDialog.sortFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class ComicListComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted category: String?,
    private val network: BikaNetworkDataSource,
) : ComicListComponent,
    ComponentContext by componentContext {

    override val comicFlow = combine(hideCategoriesFlow, sortFlow, ::Pair)
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
                else -> {
                    Pager(
                        config = PagingConfig(pageSize = 20),
                        initialKey = 1,
                    ) { ComicListPagingSource(network, category, sort) }
                        .flow
                        .map { pagingData ->
                            if (hide.isEmpty()) return@map pagingData
                            pagingData.filter { comic ->
                                for (h in hide) {
                                    for (c in comic.categories) {
                                        if (h == c) {
                                            return@filter false
                                        }
                                    }
                                }
                                return@filter true
                            }
                        }
                }
            }
        }
        .cachedIn(componentScope)

    @AssistedFactory
    interface Factory : ComicListComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            category: String?,
        ): ComicListComponentImpl
    }
}
