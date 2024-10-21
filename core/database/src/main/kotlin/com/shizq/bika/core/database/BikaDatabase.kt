package com.shizq.bika.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shizq.bika.core.database.dao.RecentWatchedComicQueryDao
import com.shizq.bika.core.database.model.RecentWatchedComicQueryEntity
import com.shizq.bika.core.database.util.InstantConverter

@Database(
    entities = [
        RecentWatchedComicQueryEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
)
internal abstract class BikaDatabase : RoomDatabase() {
    abstract fun recentWatchedComicQueryDao(): RecentWatchedComicQueryDao
}
