package com.shizq.bika.feature.comic.di

import com.shizq.bika.feature.comic.list.ComicListComponent
import com.shizq.bika.feature.comic.list.ComicListComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ComicModule {
    @Binds
    fun componentFactory(impl: ComicListComponentImpl.Factory): ComicListComponent.Factory
}
