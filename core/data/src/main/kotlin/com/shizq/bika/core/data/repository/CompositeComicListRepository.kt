package com.shizq.bika.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shizq.bika.core.data.paging.ComicListPagingSource
import com.shizq.bika.core.data.paging.ComicRandomPagingSource
import com.shizq.bika.core.data.paging.ComicRecommendPagingSource
import com.shizq.bika.core.data.paging.FavouritePagingSource
import com.shizq.bika.core.datastore.BikaInterestsDataSource
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.di.ApplicationScope
import com.shizq.bika.core.network.model.Comics
import com.shizq.bika.core.network.model.Sort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompositeComicListRepository @Inject constructor(
    private val userInterests: BikaInterestsDataSource,
    private val recentlyViewedComicRepository: RecentlyViewedComicRepository,
    @ApplicationScope private val scope: CoroutineScope,
    private val comicRecommendPagingSource: ComicRecommendPagingSource,
    private val comicRandomPagingSource: ComicRandomPagingSource,
    private val favouritePagingSource: FavouritePagingSource,
    private val comicListPagingSourceFactory: ComicListPagingSource.Factory,
) {
    fun getPagingFlowByCategories(
        comics: Comics,
        page: Int? = null,
    ): Flow<PagingData<ComicResource>> =
        Pager(PagingConfig(20), 1) {
            when (comics.category) {
                "recommend" -> comicRecommendPagingSource
                "random" -> comicRandomPagingSource
                "recently" -> recentlyViewedComicRepository.getRecentWatchedComicQueries()
                "favourite" -> favouritePagingSource
                else -> comicListPagingSourceFactory(
                    sort = Sort.SORT_DEFAULT,
                    page = page,
                    comics = comics,
                )
            }
        }.flow
            // .map { pagingData -> pagingData.filter { shouldHideCategory(category) } }
            .cachedIn(scope)
}
