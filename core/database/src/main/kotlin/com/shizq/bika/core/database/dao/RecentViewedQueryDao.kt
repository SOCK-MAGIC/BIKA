package com.shizq.bika.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.shizq.bika.core.database.model.RecentViewedQueryEntity

@Dao
interface RecentViewedQueryDao {
    @Query(value = "SELECT * FROM recentViewedQueries ORDER BY queriedDate DESC")
    fun getRecentViewedQueryEntities(): PagingSource<Int, RecentViewedQueryEntity>

    @Upsert
    suspend fun insertOrReplaceRecentViewedQuery(recentViewedQueryEntity: RecentViewedQueryEntity)

    @Query(value = "DELETE FROM recentViewedQueries")
    suspend fun clearRecentSearchQueries()
}
