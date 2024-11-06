package com.shizq.bika.feature.interest

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface InterestComponent {
    val interestUiState: StateFlow<InterestsUiState>
    val topicsUiState: StateFlow<TopicsUiState>
    fun updateTopicSelection(title: String, state: Boolean)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): InterestComponent
    }
}
