package com.shizq.bika.feature.ranking

import com.arkivanov.decompose.ComponentContext

interface RankingComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): RankingComponent
    }
}
