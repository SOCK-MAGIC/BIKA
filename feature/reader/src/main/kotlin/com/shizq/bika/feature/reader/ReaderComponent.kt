package com.shizq.bika.feature.reader

import com.arkivanov.decompose.ComponentContext

interface ReaderComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            id: String,
        ): ReaderComponent
    }
}
