package com.shizq.bika.feature.comic.info

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ComicInfoComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted id: String,
) : ComicInfoComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : ComicInfoComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            id: String,
        ): ComicInfoComponentImpl
    }
}
