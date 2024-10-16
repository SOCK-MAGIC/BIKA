package com.shizq.bika.feature.ranking.di

import com.shizq.bika.feature.ranking.RankingComponent
import com.shizq.bika.feature.ranking.RankingComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RankingModule {
    @Binds
    fun componentFactory(impl: RankingComponentImpl.Factory): RankingComponent.Factory
}
