package com.shizq.bika.feature.comment

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.data.model.Comments
import kotlinx.coroutines.flow.Flow

interface CommentComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            comicId: String,
        ): CommentComponent
    }

    val pagingDataFlow: Flow<androidx.paging.PagingData<Comments>>
}
