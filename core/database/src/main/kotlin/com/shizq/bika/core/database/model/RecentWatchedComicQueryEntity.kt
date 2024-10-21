package com.shizq.bika.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "recentWatchedComicQueries",
)
data class RecentWatchedComicQueryEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo
    val imageUrl: String,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val author: String,
    @ColumnInfo
    val categories: String,
    @ColumnInfo
    val finished: Boolean,
    @ColumnInfo
    val epsCount: Int,
    @ColumnInfo
    val pagesCount: Int,
    @ColumnInfo
    val likeCount: Int,
    @ColumnInfo
    val queriedDate: Instant,
)
