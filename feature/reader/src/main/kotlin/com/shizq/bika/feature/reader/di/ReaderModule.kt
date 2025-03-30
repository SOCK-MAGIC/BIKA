package com.shizq.bika.feature.reader.di

import com.shizq.bika.feature.reader.BikaReaderController
import com.shizq.bika.feature.reader.ReaderComponent
import com.shizq.bika.feature.reader.ReaderComponentImpl
import com.shizq.bika.feature.reader.ReaderController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ReaderModule {
    @Binds
    fun componentFactory(impl: ReaderComponentImpl.Factory): ReaderComponent.Factory

    @Binds
    fun readerControllerBind(impl: BikaReaderController): ReaderController
}
