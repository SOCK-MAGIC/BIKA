package com.shizq.bika.feature.ranking

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface RankingComponent {
    val rankingUiState: StateFlow<RankUiState>

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): RankingComponent
    }
}
