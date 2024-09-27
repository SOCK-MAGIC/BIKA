package com.shizq.bika.feature.interest

import com.arkivanov.decompose.ComponentContext

interface InterestComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): InterestComponent
    }
}
