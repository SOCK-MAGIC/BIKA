package com.shizq.bika.feature.search

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {
    val searchQuery: StateFlow<String>

    val recentSearchQueriesUiState: StateFlow<String>
    fun onSearchQueryChanged(query: String)
    fun onSearchTriggered(query: String)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): SearchComponent
    }
}
