package com.shizq.bika.feature.interest.di

import com.shizq.bika.feature.interest.InterestComponent
import com.shizq.bika.feature.interest.InterestComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InterestModule {
    @Binds
    fun componentFactory(impl: InterestComponentImpl.Factory): InterestComponent.Factory
}
