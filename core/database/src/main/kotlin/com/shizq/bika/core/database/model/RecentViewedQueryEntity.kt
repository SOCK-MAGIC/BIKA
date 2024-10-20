package com.shizq.bika.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "recentViewedQueries",
)
data class RecentViewedQueryEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo
    val queriedDate: Instant,
)
