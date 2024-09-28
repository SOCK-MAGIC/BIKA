package com.shizq.bika.navigation

import com.shizq.bika.navigation.RootComponent.Child.*
import androidx.tracing.trace
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.shizq.bika.feature.comic.ComicComponent
import com.shizq.bika.feature.interest.InterestComponent
import com.shizq.bika.feature.signin.SignInComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val signInComponentFactory: SignInComponent.Factory,
    private val interestComponentFactory: InterestComponent.Factory,
    private val comicComponentFactory: ComicComponent.Factory,
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.SignIn,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        trace("Navigation: $config") {
            when (config) {
                Config.SignIn -> SignIn(signInComponentFactory(componentContext))
                Config.Interest -> Interest(interestComponentFactory(componentContext))
                Config.Comic -> Comic(comicComponentFactory(componentContext))
            }
        }

    override fun navigationToSignIn() {
        navigation.push(Config.SignIn)
    }

    override fun navigationToInterest() {
        navigation.push(Config.Interest)
    }

    override fun onBack() {
        navigation.pop()
    }

    @Serializable
    private sealed interface Config {
        data object SignIn : Config
        data object Interest : Config
        data object Comic : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultRootComponent
    }
}
