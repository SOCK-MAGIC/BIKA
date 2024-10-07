package com.shizq.bika.feature.splash

import com.arkivanov.decompose.ComponentContext

interface SplashComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): SplashComponent
    }

    val hasToken: Boolean
}
