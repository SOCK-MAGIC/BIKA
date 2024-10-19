package com.shizq.bika.feature.interest

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface InterestComponent {
    val interestUiState: StateFlow<InterestsUiState>
    val interestVisibilityUiState: StateFlow<Map<String, Boolean>>
    fun updateInterestVisibility(title: String, state: Boolean)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): InterestComponent
    }
}
