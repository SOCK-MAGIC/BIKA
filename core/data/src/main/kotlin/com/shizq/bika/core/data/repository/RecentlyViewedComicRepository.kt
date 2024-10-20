package com.shizq.bika.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.shizq.bika.core.data.model.RecentViewedQuery
import com.shizq.bika.core.data.model.asExternalModel
import com.shizq.bika.core.database.dao.RecentViewedQueryDao
import com.shizq.bika.core.database.model.RecentViewedQueryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

class RecentlyViewedComicRepository @Inject constructor(
    private val recentSearchQueryDao: RecentViewedQueryDao,
) {
    suspend fun insertOrReplaceRecentWatchedComic(id: String) {
        recentSearchQueryDao.insertOrReplaceRecentViewedQuery(
            RecentViewedQueryEntity(
                id = id,
                queriedDate = Clock.System.now(),
            ),
        )
    }

    fun getRecentWatchedComicQueries(): Flow<PagingData<RecentViewedQuery>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { recentSearchQueryDao.getRecentViewedQueryEntities() },
        ).flow
            .map { pagingData -> pagingData.map { it.asExternalModel() } }
    }

    suspend fun clearRecentWatchedComic() = recentSearchQueryDao.clearRecentSearchQueries()
}