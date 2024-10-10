package com.shizq.bika.feature.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.pagingsource.SearchPagingSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class SearchComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
    private val searchPagingSourceFactory: SearchPagingSource.Factory,
) : SearchComponent,
    ComponentContext by componentContext {
    override val recentSearchQueriesUiState: StateFlow<String> = MutableStateFlow("")
    private val searchQueryHandle = instanceKeeper.getOrCreateSimple { MutableStateFlow("") }

    override val searchQuery = searchQueryHandle.asStateFlow()

    override val searchUiState = searchQueryHandle
        .filter { it.isNotEmpty() }
        .flatMapLatest {
            Pager(
                PagingConfig(pageSize = 20),
            ) { searchPagingSourceFactory(it) }
                .flow
                .cachedIn(componentScope)
        }

    override fun onSearchQueryChanged(query: String) {
        searchQueryHandle.value = query
    }

    override fun onSearchTriggered(query: String) {
        componentScope.launch {
            network.advancedSearch(query, 1)
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
