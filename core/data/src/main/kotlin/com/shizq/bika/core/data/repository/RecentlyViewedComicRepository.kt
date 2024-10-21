package com.shizq.bika.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.shizq.bika.core.data.model.asExternalModel
import com.shizq.bika.core.database.dao.RecentWatchedComicQueryDao
import com.shizq.bika.core.database.model.RecentWatchedComicQueryEntity
import com.shizq.bika.core.model.ComicResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

class RecentlyViewedComicRepository @Inject constructor(
    private val recentSearchQueryDao: RecentWatchedComicQueryDao,
) {
    suspend fun insertOrReplaceRecentWatchedComic(resource: ComicResource) {
        recentSearchQueryDao.insertOrReplaceRecentViewedQuery(
            RecentWatchedComicQueryEntity(
                id = resource.id,
                imageUrl = resource.imageUrl,
                title = resource.title,
                author = resource.author,
                categories = resource.categories.joinToString(),
                finished = resource.finished,
                epsCount = resource.epsCount,
                pagesCount = resource.pagesCount,
                likeCount = resource.likeCount,
                queriedDate = Clock.System.now(),
            ),
        )
    }

    fun getRecentWatchedComicQueries(): Flow<PagingData<ComicResource>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { recentSearchQueryDao.getRecentViewedQueryEntities() },
        ).flow
            .map { pagingData -> pagingData.map { it.asExternalModel() } }
    }

    suspend fun clearRecentWatchedComic() = recentSearchQueryDao.clearRecentSearchQueries()
}