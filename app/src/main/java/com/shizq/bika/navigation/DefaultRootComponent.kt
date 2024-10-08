package com.shizq.bika.navigation

import androidx.tracing.trace
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.children.SimpleNavigation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.shizq.bika.feature.comic.info.ComicInfoComponent
import com.shizq.bika.feature.comic.list.ComicListComponent
import com.shizq.bika.feature.interest.InterestComponent
import com.shizq.bika.feature.reader.ReaderComponent
import com.shizq.bika.feature.signin.SignInComponent
import com.shizq.bika.feature.splash.SplashComponent
import com.shizq.bika.navigation.RootComponent.Child.*
import com.shizq.bika.router.ChildDrawer
import com.shizq.bika.router.childDrawer
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
    private val drawerComponentFactory: DrawerComponent.Factory,
    private val splashComponentFactory: SplashComponent.Factory,
    private val readerComponentFactory: ReaderComponent.Factory,
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Splash,
            handleBackButton = true,
            childFactory = ::child,
        )

    private val drawerNavigation = SimpleNavigation<Boolean>()
    override val drawer: Value<ChildDrawer<DrawerComponent>> =
        childDrawer(
            source = drawerNavigation,
            childFactory = { drawerComponentFactory(componentContext) },
        )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        trace("Navigation: $config") {
            when (config) {
                Config.Splash -> Splash(splashComponentFactory(componentContext))
                Config.SignIn -> SignIn(signInComponentFactory(componentContext))
                Config.Interest -> Interest(interestComponentFactory(componentContext))
                is Config.ComicList -> ComicList(
                    comicListComponentFactory(componentContext, config.title),
                )

                is Config.ComicInfo -> ComicInfo(
                    comicInfoComponentFactory(componentContext, config.id),
                )

                is Config.Reader -> Reader(readerComponentFactory(componentContext, config.id))
            }
        }

    override fun navigationToSignIn() {
        navigation.push(Config.SignIn)
    }

    override fun navigationToInterest() {
        navigation.popTo(0)
        navigation.push(Config.Interest)
    }

    override fun navigationToComicList(tag: String, title: String) {
        navigation.push(Config.ComicList(tag, title))
    }

    override fun navigationToComicInfo(id: String) {
        navigation.push(Config.ComicInfo(id))
    }

    override fun navigationToReader(id: String) {
        navigation.push(Config.Reader(id))
    }

    override fun onBack() {
        navigation.pop()
    }

    override fun setDrawerState(isOpen: Boolean) {
        drawerNavigation.navigate(isOpen)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object SignIn : Config

        @Serializable
        data object Interest : Config

        @Serializable
        data object Splash : Config

        @Serializable
        data class ComicList(val tag: String, val title: String) : Config

        @Serializable
        data class ComicInfo(val id: String) : Config

        @Serializable
        data class Reader(val id: String) : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultRootComponent
    }
}
