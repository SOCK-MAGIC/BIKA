package com.shizq.bika.feature.search.di

import com.shizq.bika.feature.search.SearchComponent
import com.shizq.bika.feature.search.SearchComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SearchModule {
    @Binds
    fun componentFactory(impl: SearchComponentImpl.Factory): SearchComponent.Factory
}
