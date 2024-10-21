package com.shizq.bika.feature.comic.recent

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.data.repository.RecentlyViewedComicRepository
import com.shizq.bika.core.model.ComicResource

class RecentlyPagingSource(
    private val recentlyViewedComicRepository: RecentlyViewedComicRepository,
) : PagingSource<Int, ComicResource>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicResource> {
        recentlyViewedComicRepository.getRecentWatchedComicQueries()
        TODO()
    }

    override fun getRefreshKey(state: PagingState<Int, ComicResource>): Int? = null
}
