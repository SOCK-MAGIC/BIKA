package com.shizq.bika.core.data.paging

import com.shizq.bika.core.data.util.asComicResource
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.Sort
import javax.inject.Inject

class FavouritePagingSource@Inject constructor(
    private val network: BikaNetworkDataSource,
    // private val sort: Sort,
) : BikaComicListPagingSource() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicResource> {
        val nextPageNumber = params.key ?: 1
        val comicList = network.favouriteComics(sort = Sort.SORT_DEFAULT, page = nextPageNumber)
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
}
