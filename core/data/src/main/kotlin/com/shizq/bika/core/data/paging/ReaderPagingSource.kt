package com.shizq.bika.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.data.model.PagingMetadata
import com.shizq.bika.core.model.Picture
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ReaderPagingSource @AssistedInject constructor(
    private val network: BikaNetworkDataSource,
    @Assisted private val id: String,
    @Assisted private val order: Int,
    @Assisted private val pagingMetadata: (PagingMetadata) -> Unit,
) : PagingSource<Int, Picture>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Picture> {
        val nextPageNumber = params.key ?: 1
        val comicEpPicture = network.getComicEpPictures(
            id = id,
            epOrder = order,
            page = nextPageNumber,
        )
        val (docs, limit, page, pages, total) = comicEpPicture.pages
        pagingMetadata(PagingMetadata(pages, page, total))
        return try {
            LoadResult.Page(
                data = docs.map { Picture(it.id, it.media.imageUrl) },
                prevKey = null,
                nextKey = if (page < pages) page + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Picture>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    @AssistedFactory
    interface Factory {
        operator fun invoke(
            id: String,
            order: Int,
            pagingMetadata: (PagingMetadata) -> Unit,
        ): ReaderPagingSource
    }
}
