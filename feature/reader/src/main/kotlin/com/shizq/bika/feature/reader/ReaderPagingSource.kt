package com.shizq.bika.feature.reader

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.network.BikaNetworkDataSource

class ReaderPagingSource(
    private val network: BikaNetworkDataSource,
    private val id: String,
) : PagingSource<Int, Picture>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Picture> {
        val nextPageNumber = params.key ?: 1
        val comicEpPicture = network.getComicEpPictures(
            id = id,
            epOrder = 1,
            page = nextPageNumber,
        )
        val (docs, limit, page, pages, total) = comicEpPicture.pages
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

    override fun getRefreshKey(state: PagingState<Int, Picture>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

data class Picture(val id: String, val url: String)
