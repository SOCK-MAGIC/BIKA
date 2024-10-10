package com.shizq.bika.feature.interest

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface InterestComponent {
    val interestUiState: StateFlow<InterestsUiState>
    val searchQuery: StateFlow<String>

    // val recentSearchQueriesUiState: StateFlow<String>
    fun onSearchQueryChanged(query: String)
    fun onSearchTriggered(query: String)

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): InterestComponent
    }
}
