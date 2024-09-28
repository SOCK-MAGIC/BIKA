package com.shizq.bika.feature.comic

import com.arkivanov.decompose.ComponentContext

interface ComicListComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            category: String,
        ): ComicListComponent
    }
}
