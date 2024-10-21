package com.shizq.bika.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.shizq.bika.core.database.model.RecentWatchedComicQueryEntity

@Dao
interface RecentWatchedComicQueryDao {
    @Query(value = "SELECT * FROM recentWatchedComicQueries ORDER BY queriedDate DESC")
    fun getRecentViewedQueryEntities(): PagingSource<Int, RecentWatchedComicQueryEntity>

    @Upsert
    suspend fun insertOrReplaceRecentViewedQuery(recentWatchedComicQueryEntity: RecentWatchedComicQueryEntity)

    @Query(value = "DELETE FROM recentWatchedComicQueries")
    suspend fun clearRecentSearchQueries()
}
