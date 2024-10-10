package com.shizq.bika.feature.signup.di

import com.shizq.bika.feature.signup.SignUpComponent
import com.shizq.bika.feature.signup.SignUpComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SignInModule {
    @Binds
    fun componentFactory(impl: SignUpComponentImpl.Factory): SignUpComponent.Factory
}
