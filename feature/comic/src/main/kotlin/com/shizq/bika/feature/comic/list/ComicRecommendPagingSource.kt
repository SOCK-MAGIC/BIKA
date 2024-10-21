package com.shizq.bika.feature.comic.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource

class ComicRecommendPagingSource(private val network: BikaNetworkDataSource) : PagingSource<Int, ComicResource>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicResource> {
        val comicList = network.getRecommend().collections
            .flatMap { collection -> collection.comics.map { it.asComicResource() } }

        return try {
            LoadResult.Page(
                data = comicList,
                prevKey = null,
                nextKey = null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ComicResource>): Int? = null
}
