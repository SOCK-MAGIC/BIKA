package com.shizq.bika.core.network.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.shizq.bika.core.network.pagingsource.SearchPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PagingSourceModule {
    @Provides
    @Singleton
    fun searchPagingSource(
        searchPagingSourceFactory: SearchPagingSource.Factory,
    ) =
        Pager(
            PagingConfig(pageSize = 20),
        ) { searchPagingSourceFactory("") }
            .flow
}
