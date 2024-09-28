package com.shizq.bika.feature.comic.di

import com.shizq.bika.feature.comic.ComicComponent
import com.shizq.bika.feature.comic.ComicComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ComicModule {
    @Binds
    fun componentFactory(impl: ComicComponentImpl.Factory): ComicComponent.Factory
}
