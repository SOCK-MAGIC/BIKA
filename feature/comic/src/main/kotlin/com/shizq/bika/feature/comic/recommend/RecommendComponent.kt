package com.shizq.bika.feature.comic.recommend

import com.arkivanov.decompose.ComponentContext

interface RecommendComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): RecommendComponent
    }
}
