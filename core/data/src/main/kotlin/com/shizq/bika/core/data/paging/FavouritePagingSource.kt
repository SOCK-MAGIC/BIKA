package com.shizq.bika.core.data.paging

import com.shizq.bika.core.data.model.PagingMetadata
import com.shizq.bika.core.data.util.asComicResource
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.Sort
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

class FavouritePagingSource @AssistedInject constructor(
    private val network: BikaNetworkDataSource,
    // private val sort: Sort,
    @Assisted private val pagingMetadata: (PagingMetadata) -> Unit,
) : BikaComicListPagingSource() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicResource> {
        val nextPageNumber = params.key ?: 1
        val comicList = network.favouriteComics(sort = Sort.SORT_DEFAULT, page = nextPageNumber)

        val page = comicList.comics.page
        val pages = comicList.comics.pages
        pagingMetadata(PagingMetadata(comicList.comics.total, page, pages))
        return try {
            LoadResult.Page(
                data = comicList.comics.asComicResource(),
                prevKey = if (page > 2) page - 1 else null,
                nextKey = if (nextPageNumber < pages) page + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(
            pagingMetadata: (PagingMetadata) -> Unit,
        ): FavouritePagingSource
    }
}
