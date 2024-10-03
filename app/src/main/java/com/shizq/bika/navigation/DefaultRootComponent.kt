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
import com.shizq.bika.feature.comic.info.ComicInfoComponent
import com.shizq.bika.feature.comic.list.ComicListComponent
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
    private val comicListComponentFactory: ComicListComponent.Factory,
    private val comicInfoComponentFactory: ComicInfoComponent.Factory,
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
                is Config.ComicList -> ComicList(
                    comicListComponentFactory(componentContext, config.title)
                )

                is Config.ComicInfo -> ComicInfo(
                    comicInfoComponentFactory(componentContext, config.id)
                )
            }
        }

    override fun navigationToSignIn() {
        navigation.push(Config.SignIn)
    }

    override fun navigationToInterest() {
        navigation.push(Config.Interest)
    }

    override fun navigationToComicList(tag: String, title: String) {
        navigation.push(Config.ComicList(tag, title))
    }

    override fun navigationToComicInfo(id: String) {
        navigation.push(Config.ComicInfo(id))
    }

    override fun onBack() {
        navigation.pop()
    }

    @Serializable
    private sealed interface Config {
        data object SignIn : Config
        data object Interest : Config
        data class ComicList(val tag: String, val title: String) : Config
        data class ComicInfo(val id: String) : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultRootComponent
    }
}
