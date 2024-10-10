package com.shizq.bika.feature.interest

import kotlinx.coroutines.flow.MutableStateFlow

class PreviewInterestComponent : InterestComponent {
    override val interestUiState: MutableStateFlow<InterestsUiState> =
        MutableStateFlow(InterestsUiState.Loading)
    override var searchQuery = MutableStateFlow("")
    override fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }

    override fun onSearchTriggered(query: String) {}
}
