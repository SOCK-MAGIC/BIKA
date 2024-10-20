package com.shizq.bika.feature.search

import com.shizq.bika.core.data.model.RecentViewedQuery

sealed interface RecentSearchQueriesUiState {
    data object Loading : RecentSearchQueriesUiState

    data class Success(
        val recentQueries: List<RecentViewedQuery> = emptyList(),
    ) : RecentSearchQueriesUiState
}
