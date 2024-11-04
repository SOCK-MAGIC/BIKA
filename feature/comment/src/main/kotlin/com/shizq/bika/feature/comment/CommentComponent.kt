package com.shizq.bika.feature.comment

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.shizq.bika.core.data.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentComponent : BackHandlerOwner {
    val pagingDataFlow: Flow<PagingData<Comment>>
    val childCommentPagingDataFlow: Flow<PagingData<Comment>>
    val commentContent: String
    fun clickedCommentId(id: String)
    fun sendComment(commentId: String)
    fun changeCommentContent(text: String)

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            comicId: String,
        ): CommentComponent
    }
}
