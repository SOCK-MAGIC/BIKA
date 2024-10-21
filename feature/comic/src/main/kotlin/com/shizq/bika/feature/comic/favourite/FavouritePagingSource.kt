package com.shizq.bika.feature.comic.favourite

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.Sort
import com.shizq.bika.feature.comic.model.asComicResource

class FavouritePagingSource(
    private val network: BikaNetworkDataSource,
    private val sort: Sort,
) : PagingSource<Int, ComicResource>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicResource> {
        val nextPageNumber = params.key ?: 1
        val comicList = network.favouriteComics(sort = sort, page = nextPageNumber,)
        val comics = comicList.comics

        val page = comicList.comics.page
        val pages = comicList.comics.pages
        val total = comicList.comics.total
        return try {
            LoadResult.Page(
                data = comics.docs.map { it.asComicResource() },
                prevKey = if (page > 2) page - 1 else null,
                nextKey = if (nextPageNumber < pages) page + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ComicResource>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}
