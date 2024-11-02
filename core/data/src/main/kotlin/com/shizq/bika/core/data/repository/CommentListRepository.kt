package com.shizq.bika.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shizq.bika.core.data.model.Comments
import com.shizq.bika.core.data.paging.CommentListPagingSource
import com.shizq.bika.core.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentListRepository @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    private val commentListPagingSourceFactory: CommentListPagingSource.Factory,
) {
    operator fun invoke(comicId: String): Flow<PagingData<Comments>> =
        Pager(PagingConfig(20), 1) {
            commentListPagingSourceFactory(comicId)
        }.flow
            .cachedIn(scope)
}
