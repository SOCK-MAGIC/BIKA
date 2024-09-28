package com.shizq.bika.feature.interest

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface InterestComponent {
    val interestUiState: StateFlow<InterestsUiState>

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): InterestComponent
    }
}
