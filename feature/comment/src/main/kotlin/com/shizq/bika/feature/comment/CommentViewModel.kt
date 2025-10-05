package com.shizq.bika.feature.comment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import com.shizq.bika.core.data.paging.PageBasedPagedSource
import com.shizq.bika.core.data.paging.Paged
import com.shizq.bika.core.data.repository.ChildCommentListRepository
import com.shizq.bika.core.data.repository.CommentListRepository
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
//    comicId: String,
    commentListRepository: CommentListRepository,
    childCommentListRepository: ChildCommentListRepository,
    private val network: BikaNetworkDataSource,
) : ViewModel() {
    private var commentId by mutableStateOf<String?>(null)
    var commentContent by mutableStateOf("")

    val pagingDataFlow = commentListRepository("comicId")

    val childCommentPagingDataFlow =
        snapshotFlow { commentId }.filterNotNull().flatMapLatest {
            childCommentListRepository(it)
        }
    val comments = PageBasedPagedSource(1) {
        val comments = network.getComments("comicId", it)
        val (docs, _, page, pages, total) = comments.comments
        Paged(total, (page * pages < total), docs)
    }
    val selfComment = ""
    fun changeCommentContent(text: String) {
        commentContent = text
    }

    fun sendComment(commentId: String) {
    }

    fun clickedCommentId(id: String) {
        Napier.i(tag = "CommentComponent") { id }
        commentId = id
    }
}
