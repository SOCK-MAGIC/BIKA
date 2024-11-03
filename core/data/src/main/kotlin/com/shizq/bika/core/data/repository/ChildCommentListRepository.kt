package com.shizq.bika.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shizq.bika.core.data.model.Comment
import com.shizq.bika.core.data.paging.ChildCommentListPagingSource
import com.shizq.bika.core.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChildCommentListRepository @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    private val childCommentListPagingSource: ChildCommentListPagingSource.Factory,
) {
    operator fun invoke(commentId: String): Flow<PagingData<Comment>> =
        Pager(PagingConfig(10), 1) {
            childCommentListPagingSource(commentId)
        }.flow
            .cachedIn(scope)
}
