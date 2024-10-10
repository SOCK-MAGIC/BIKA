package com.shizq.bika.feature.search

import com.arkivanov.decompose.ComponentContext

interface SearchComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): SearchComponent
    }
}
