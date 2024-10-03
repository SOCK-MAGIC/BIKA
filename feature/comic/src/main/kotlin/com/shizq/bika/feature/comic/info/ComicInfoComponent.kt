package com.shizq.bika.feature.comic.info

import com.arkivanov.decompose.ComponentContext

interface ComicInfoComponent {

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            id: String,
        ): ComicInfoComponent
    }
}
