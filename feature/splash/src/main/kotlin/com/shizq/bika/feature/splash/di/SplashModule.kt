package com.shizq.bika.feature.splash.di

import com.shizq.bika.feature.splash.SplashComponent
import com.shizq.bika.feature.splash.SplashComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SplashModule {
    @Binds
    fun componentFactory(impl: SplashComponentImpl.Factory): SplashComponent.Factory
}
