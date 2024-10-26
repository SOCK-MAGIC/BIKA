package com.shizq.bika.feature.comment.di

import com.shizq.bika.feature.signup.CommentComponent
import com.shizq.bika.feature.signup.CommentComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CommentModule {
    @Binds
    fun componentFactory(impl: CommentComponentImpl.Factory): CommentComponent.Factory
}
