package com.shizq.bika.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.data.model.Comments
import com.shizq.bika.core.data.model.asCommentList
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CommentListPagingSource @AssistedInject constructor(
    private val network: BikaNetworkDataSource,
    @Assisted private val comicId: String,
) : PagingSource<Int, Comments>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comments> {
        val nextPageNumber = params.key ?: 1
        val commentList = network.getComments(comicId, nextPageNumber)
        val page = commentList.comments.page.toIntOrNull() ?: 0
        val pages = commentList.comments.pages

        return try {
            LoadResult.Page(
                data = commentList.asCommentList(),
                prevKey = if (page > 2) page - 1 else null,
                nextKey = if (nextPageNumber < pages) page + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Comments>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    @AssistedFactory
    interface Factory {
        operator fun invoke(
            @Assisted comicId: String,
        ): CommentListPagingSource
    }
}
