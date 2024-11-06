package com.shizq.bika.feature.comment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.data.repository.ChildCommentListRepository
import com.shizq.bika.core.data.repository.CommentListRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

class CommentComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted comicId: String,
    commentListRepository: CommentListRepository,
    childCommentListRepository: ChildCommentListRepository,
) : CommentComponent,
    ComponentContext by componentContext {
    private var commentId by mutableStateOf<String?>(null)
    override var commentContent by mutableStateOf("")

    override val pagingDataFlow = commentListRepository(comicId)

    override val childCommentPagingDataFlow =
        snapshotFlow { commentId }.filterNotNull().flatMapLatest {
            childCommentListRepository(it)
        }

    override fun changeCommentContent(text: String) {
        commentContent = text
    }

    override fun sendComment(commentId: String) {
    }

    override fun clickedCommentId(id: String) {
        Napier.i(tag = "CommentComponent") { id }
        commentId = id
    }

    @AssistedFactory
    interface Factory : CommentComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            comicId: String,
        ): CommentComponentImpl
    }
}
