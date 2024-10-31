package com.shizq.bika.core.data.paging

import androidx.paging.PagingSource.LoadResult
import com.shizq.bika.core.data.model.PagingMetadata
import com.shizq.bika.core.data.util.asComicResource
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.Sort

class SearchPagingSource(
    private val network: BikaNetworkDataSource,
    private val query: String,
    private val sort: Sort,
    private val pagingMetadata: (PagingMetadata) -> Unit,
) : BikaComicListPagingSource() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicResource> {
        val nextPageNumber = params.key ?: 1
        val comicList = network.advancedSearch(
            query = query,
            sort = sort,
            page = nextPageNumber,
        )
        val page = comicList.comics.page
        val pages = comicList.comics.pages
        pagingMetadata(PagingMetadata(comicList.comics.total, page, pages))
        val comics = comicList.comics.asComicResource()
        return try {
            LoadResult.Page(
                data = comics,
                prevKey = if (page > 2) page - 1 else null,
                nextKey = if (nextPageNumber < pages) page + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
