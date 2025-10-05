package com.shizq.bika.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.shizq.bika.core.data.paging.SearchPagingSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.Sort
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val network: BikaNetworkDataSource,
    query: String?,
) : ViewModel() {
    val recentSearchQueriesUiState: StateFlow<String> = MutableStateFlow("")
    private val searchQueryHandle = MutableStateFlow(query ?: "")

    val searchQuery = searchQueryHandle.asStateFlow()

    @OptIn(FlowPreview::class)
    val searchResultUiState = searchQueryHandle
        .debounce(1000)
        .filterNot { it.isEmpty() }
        .flatMapLatest {
            Pager(
                PagingConfig(pageSize = 20),
            ) { SearchPagingSource(network, it, Sort.SORT_DEFAULT) {} }
                .flow
                .cachedIn(viewModelScope)
        }

    fun onSearchQueryChanged(query: String) {
        searchQueryHandle.value = query
    }

    fun onSearchTriggered(query: String) {
        viewModelScope.launch {
        }
    }
}

private const val SEARCH_MIN_FTS_ENTITY_COUNT = 1
