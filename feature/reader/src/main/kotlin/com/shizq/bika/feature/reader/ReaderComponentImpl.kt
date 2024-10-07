package com.shizq.bika.feature.reader

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ReaderComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : ReaderComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : ReaderComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            id: String,
        ): ReaderComponentImpl
    }
}
