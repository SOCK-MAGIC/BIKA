package com.shizq.bika.feature.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.ComicInSearch
import com.shizq.bika.core.network.pagingsource.SearchPagingSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
) : SearchComponent,
    ComponentContext by componentContext {
    override val recentSearchQueriesUiState: StateFlow<String> = MutableStateFlow("")
    private val searchQueryHandle = instanceKeeper.getOrCreateSimple { MutableStateFlow("") }

    override val searchQuery = searchQueryHandle.asStateFlow()

    @OptIn(FlowPreview::class)
    override val searchResultUiState = searchQueryHandle
        .debounce(1000)
        .filterNot { it.isEmpty() }
        .flatMapLatest {
            Pager(
                PagingConfig(pageSize = 20),
            ) { SearchPagingSource(network, it) }
                .flow
                .map { pagingData ->
                    pagingData.map { it.asComicResource() }
                }
                .cachedIn(componentScope)
        }

    override fun onSearchQueryChanged(query: String) {
        searchQueryHandle.value = query
    }

    override fun onSearchTriggered(query: String) {
        componentScope.launch {
        }
    }

    @AssistedFactory
    interface Factory : SearchComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): SearchComponentImpl
    }
}

private const val SEARCH_MIN_FTS_ENTITY_COUNT = 1
private fun ComicInSearch.Comics.Doc.asComicResource() =
    ComicResource(
        id,
        thumb.imageUrl,
        title,
        author,
        categories,
        finished,
        0,
        0,
        likesCount,
    )
