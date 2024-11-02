package com.shizq.bika.feature.comment

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.data.repository.CommentListRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CommentComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted comicId: String,
    commentListRepository: CommentListRepository,
) : CommentComponent,
    ComponentContext by componentContext {
    override val pagingDataFlow = commentListRepository(comicId)

    @AssistedFactory
    interface Factory : CommentComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            comicId: String,
        ): CommentComponentImpl
    }
}
