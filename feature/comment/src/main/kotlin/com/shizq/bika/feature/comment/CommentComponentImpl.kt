package com.shizq.bika.feature.comment

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CommentComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : CommentComponent,
    ComponentContext by componentContext {
    @AssistedFactory
    interface Factory : CommentComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): CommentComponentImpl
    }
}
