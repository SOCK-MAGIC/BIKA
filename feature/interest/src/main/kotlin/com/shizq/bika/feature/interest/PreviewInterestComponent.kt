package com.shizq.bika.feature.interest

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewInterestComponent : InterestComponent {
    override val interestUiState: MutableStateFlow<InterestsUiState> =
        MutableStateFlow(InterestsUiState.Loading)
    override val interestVisibilityUiState: StateFlow<Map<String, Boolean>>
        get() = MutableStateFlow(mapOf())

    override fun updateInterestVisibility(title: String, state: Boolean) {
    }
}
