package com.shizq.bika.di

import com.shizq.bika.navigation.DefaultRootComponent
import com.shizq.bika.navigation.RootComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RootModule {
    @Binds
    fun componentRootFactory(impl: DefaultRootComponent.Factory): RootComponent.Factory
}