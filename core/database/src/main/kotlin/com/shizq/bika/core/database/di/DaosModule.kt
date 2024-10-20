package com.shizq.bika.core.database.di

import com.shizq.bika.core.database.BikaDatabase
import com.shizq.bika.core.database.dao.RecentViewedQueryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class DaosModule {
    @Provides
    fun providesRecentViewedQueryDao(
        database: BikaDatabase,
    ): RecentViewedQueryDao = database.recentViewedQueryDao()
}
