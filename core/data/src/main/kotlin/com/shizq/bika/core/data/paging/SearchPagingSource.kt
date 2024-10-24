package com.shizq.bika.core.data.paging

import com.shizq.bika.core.data.util.asComicResource
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.Sort

class SearchPagingSource(
    private val network: BikaNetworkDataSource,
    private val query: String,
    private val sort: Sort,
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
        val total = comicList.comics.total
        val comics = comicList.comics.docs.map { it.asComicResource() }
        return try {
            LoadResult.Page(
                data = comics,
                prevKey = null,
                nextKey = if (nextPageNumber < pages) page + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
