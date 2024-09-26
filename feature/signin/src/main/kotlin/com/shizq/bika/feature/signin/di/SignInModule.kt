package com.shizq.bika.feature.signin.di

import com.shizq.bika.feature.signin.SignInComponent
import com.shizq.bika.feature.signin.SignInComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SignInModule {
    @Binds
    fun componentFactory(impl: SignInComponentImpl.Factory): SignInComponent.Factory
}