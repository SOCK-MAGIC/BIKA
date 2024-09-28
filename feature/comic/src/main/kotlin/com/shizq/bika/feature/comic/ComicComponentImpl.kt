package com.shizq.bika.feature.comic

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ComicComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : ComicComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : ComicComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): ComicComponentImpl
    }
}
