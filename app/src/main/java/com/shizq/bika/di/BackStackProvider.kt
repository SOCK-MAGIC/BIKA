package com.shizq.bika.di

import com.shizq.bika.core.navigation.BikaBackStack
import com.shizq.bika.core.navigation.BikaNavKey
import com.shizq.bika.navigation.TopLevelDestination
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackStackProvider {
    @Provides
    @Singleton
    fun provideNiaBackStack(): BikaBackStack =
        BikaBackStack(startKey = TopLevelDestination.FOR_YOU.key)


    /**
     * Registers feature modules' polymorphic serializers to support
     * feature keys' save and restore by savedstate
     * in [com.google.samples.apps.nowinandroid.core.navigation.NiaBackStackViewModel].
     */
    @Provides
    @Singleton
    fun provideSerializersModule(
        polymorphicModuleBuilders: Set<@JvmSuppressWildcards PolymorphicModuleBuilder<BikaNavKey>.() -> Unit>,
    ): SerializersModule = SerializersModule {
        polymorphic(BikaNavKey::class) {
            polymorphicModuleBuilders.forEach { it() }
        }
    }
}