package com.shizq.bika.feature.interest

import kotlinx.coroutines.flow.MutableStateFlow

class PreviewInterestComponent : InterestComponent {
    override val interestUiState: MutableStateFlow<InterestsUiState> =
        MutableStateFlow(InterestsUiState.Loading)
}
