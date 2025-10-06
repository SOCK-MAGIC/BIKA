package com.shizq.bika.di

import androidx.navigation3.runtime.EntryProviderBuilder
import com.shizq.bika.core.navigation.BikaBackStack
import com.shizq.bika.core.navigation.BikaNavKey
import com.shizq.bika.navigation.TopLevelDestination
import com.shizq.bika.router.InterestsRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackStackProvider {
    @Provides
    @Singleton
    fun provideBikaBackStack(): BikaBackStack =
        BikaBackStack(TopLevelDestination.Interests.key)

    @Provides
    @Singleton
    fun provideSerializersModule(
        polymorphicModuleBuilders: Set<@JvmSuppressWildcards PolymorphicModuleBuilder<BikaNavKey>.() -> Unit>,
    ): SerializersModule = SerializersModule {
        polymorphic(BikaNavKey::class) {
            polymorphicModuleBuilders.forEach { it() }
        }
    }

    @Provides
    @IntoSet
    fun provideInterestsPolymorphicModuleBuilder(): PolymorphicModuleBuilder<@JvmSuppressWildcards BikaNavKey>.() -> Unit =
        {
            subclass(InterestsRoute::class, InterestsRoute.serializer())
        }

    @Provides
    @IntoSet
    fun provideInterestsEntryProviderBuilder(
        backStack: BikaBackStack,
    ): EntryProviderBuilder<BikaNavKey>.() -> Unit = {
        entry<InterestsRoute> { key ->
//            val viewModel = hiltViewModel<InterestsViewModel, InterestsViewModel.Factory> {
//                it.create(key)

//            InterestScreen()
        }
    }
}