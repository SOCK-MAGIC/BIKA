package com.shizq.bika.core.data.paging

import androidx.paging.PagingState
import com.shizq.bika.core.data.util.asComicResource
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import javax.inject.Inject

class ComicRecommendPagingSource @Inject constructor(
    private val network: BikaNetworkDataSource,
) : BikaComicListPagingSource() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicResource> = try {
        val comicList = network.getRecommend().collections
            .flatMap { collection -> collection.comics.map { it.asComicResource() } }
        LoadResult.Page(
            data = comicList,
            prevKey = null,
            nextKey = null,
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, ComicResource>): Int? = null
}
