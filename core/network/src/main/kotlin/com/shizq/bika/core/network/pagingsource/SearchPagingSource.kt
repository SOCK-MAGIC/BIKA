package com.shizq.bika.core.network.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.ComicInSearch

class SearchPagingSource (
    private val network: BikaNetworkDataSource,
    private val query: String,
) : PagingSource<Int, ComicInSearch.Comics.Doc>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicInSearch.Comics.Doc> {
        val nextPageNumber = params.key ?: 1
        val comicList = network.advancedSearch(
            query = query,
            page = nextPageNumber,
        )
        val comics = comicList.comics
        return try {
            LoadResult.Page(
                data = comics.docs,
                prevKey = null,
                nextKey = if (nextPageNumber < comics.pages) comics.page + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ComicInSearch.Comics.Doc>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}

