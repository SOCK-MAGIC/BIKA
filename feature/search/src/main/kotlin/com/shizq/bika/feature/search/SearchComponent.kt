package com.shizq.bika.feature.search

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.model.ComicResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {
    val searchQuery: StateFlow<String>

    val recentSearchQueriesUiState: StateFlow<String>
    val searchResultUiState: Flow<PagingData<ComicResource>>
    fun onSearchQueryChanged(query: String)
    fun onSearchTriggered(query: String)

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            query: String?,
        ): SearchComponent
    }
}
