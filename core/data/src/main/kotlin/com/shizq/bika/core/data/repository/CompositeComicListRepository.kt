package com.shizq.bika.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.shizq.bika.core.data.model.PagingMetadata
import com.shizq.bika.core.data.paging.ComicListPagingSource
import com.shizq.bika.core.data.paging.ComicRandomPagingSource
import com.shizq.bika.core.data.paging.ComicRecommendPagingSource
import com.shizq.bika.core.data.paging.FavouritePagingSource
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.di.ApplicationScope
import com.shizq.bika.core.network.model.Comics
import com.shizq.bika.core.network.model.Sort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CompositeComicListRepository @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    private val recentlyViewedComicRepository: RecentlyViewedComicRepository,
    private val comicRecommendPagingSource: ComicRecommendPagingSource,
    private val comicRandomPagingSource: ComicRandomPagingSource,
    private val favouritePagingSourceFactory: FavouritePagingSource.Factory,
    private val comicListPagingSourceFactory: ComicListPagingSource.Factory,
) {

    operator fun invoke(
        comics: Comics,
        page: Int? = null,
        sort: Sort,
        hide: Set<String>,
        pagingMetadata: (PagingMetadata) -> Unit,
    ): Flow<PagingData<ComicResource>> =
        Pager(PagingConfig(20), 1) {
            when (comics.category) {
                "recommend" -> comicRecommendPagingSource
                "random" -> comicRandomPagingSource
                "recently" -> recentlyViewedComicRepository.getRecentWatchedComicQueries()
                "favourite" -> favouritePagingSourceFactory(sort, pagingMetadata)
                else -> comicListPagingSourceFactory(
                    sort = sort,
                    comics = comics,
                    pagingMetadata = pagingMetadata,
                    page = page,
                )
            }
        }.flow
            .map { pagingData ->
                if (comics.category == "recently" || comics.category == "favourite") {
                    return@map pagingData
                } else {
                    if (hide.isEmpty()) return@map pagingData
                    pagingData.filter { resource ->
                        resource.categories.any { !hide.contains(it) }
                    }
                }
            }
            .cachedIn(scope)
}
