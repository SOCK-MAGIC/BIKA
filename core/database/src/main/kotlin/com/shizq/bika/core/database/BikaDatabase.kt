package com.shizq.bika.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shizq.bika.core.database.dao.RecentViewedQueryDao
import com.shizq.bika.core.database.model.RecentViewedQueryEntity
import com.shizq.bika.core.database.util.InstantConverter

@Database(
    entities = [
        RecentViewedQueryEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
)
internal abstract class BikaDatabase : RoomDatabase() {
    abstract fun recentViewedQueryDao(): RecentViewedQueryDao
}
