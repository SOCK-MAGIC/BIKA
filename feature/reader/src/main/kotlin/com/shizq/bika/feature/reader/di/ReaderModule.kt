package com.shizq.bika.feature.reader.di

import com.shizq.bika.feature.reader.ReaderComponent
import com.shizq.bika.feature.reader.ReaderComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ReaderModule {
    @Binds
    fun componentFactory(impl: ReaderComponentImpl.Factory): ReaderComponent.Factory
}
