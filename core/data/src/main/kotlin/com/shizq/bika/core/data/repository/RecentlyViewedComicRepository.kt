package com.shizq.bika.core.data.repository

import androidx.paging.PagingSource
import com.shizq.bika.core.data.model.asExternalModel
import com.shizq.bika.core.data.paging.MappingPagingSource
import com.shizq.bika.core.database.dao.RecentWatchedComicQueryDao
import com.shizq.bika.core.database.model.RecentWatchedComicQueryEntity
import com.shizq.bika.core.model.ComicResource
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

    fun getRecentWatchedComicQueries(): PagingSource<Int, ComicResource> {
        return MappingPagingSource(
            originalSource = recentSearchQueryDao.getRecentViewedQueryEntities(),
            mapper = { it.asExternalModel() },
        )
    }

    suspend fun clearRecentWatchedComic() = recentSearchQueryDao.clearRecentSearchQueries()
}
