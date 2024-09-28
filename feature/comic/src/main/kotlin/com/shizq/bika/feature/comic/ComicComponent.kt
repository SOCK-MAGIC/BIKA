package com.shizq.bika.feature.comic

import com.arkivanov.decompose.ComponentContext

interface ComicComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): ComicComponent
    }
}
