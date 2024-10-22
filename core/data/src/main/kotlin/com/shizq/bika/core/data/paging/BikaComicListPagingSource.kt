package com.shizq.bika.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.model.ComicResource

abstract class BikaComicListPagingSource : PagingSource<Int, ComicResource>() {
    override fun getRefreshKey(state: PagingState<Int, ComicResource>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}
