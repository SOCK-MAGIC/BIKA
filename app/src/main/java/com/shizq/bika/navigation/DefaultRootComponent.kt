package com.shizq.bika.navigation

import androidx.tracing.trace
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.children.SimpleNavigation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.shizq.bika.core.network.model.Comics
import com.shizq.bika.feature.comic.info.ComicInfoComponent
import com.shizq.bika.feature.comic.list.ComicListComponent
import com.shizq.bika.feature.comment.CommentComponent
import com.shizq.bika.feature.interest.InterestComponent
import com.shizq.bika.feature.ranking.RankingComponent
import com.shizq.bika.feature.reader.ReaderComponent
import com.shizq.bika.feature.search.SearchComponent
import com.shizq.bika.feature.signin.SignInComponent
import com.shizq.bika.feature.splash.SplashComponent
import com.shizq.bika.navigation.RootComponent.Child.ComicInfo
import com.shizq.bika.navigation.RootComponent.Child.ComicList
import com.shizq.bika.navigation.RootComponent.Child.Comment
import com.shizq.bika.navigation.RootComponent.Child.Interest
import com.shizq.bika.navigation.RootComponent.Child.Ranking
import com.shizq.bika.navigation.RootComponent.Child.Reader
import com.shizq.bika.navigation.RootComponent.Child.Search
import com.shizq.bika.navigation.RootComponent.Child.SignIn
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
    private val rankingComponentFactory: RankingComponent.Factory,
    private val searchComponentFactory: SearchComponent.Factory,
    private val commentComponentFactory: CommentComponent.Factory,
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
                Config.Splash -> RootComponent.Child.Splash(splashComponentFactory(componentContext))
                Config.SignIn -> SignIn(signInComponentFactory(componentContext))
                Config.Interest -> Interest(interestComponentFactory(componentContext))
                is Config.ComicList -> ComicList(
                    comicListComponentFactory(componentContext, config.comics),
                )

                is Config.ComicInfo -> ComicInfo(
                    comicInfoComponentFactory(componentContext, config.id),
                )

                is Config.Reader -> Reader(
                    readerComponentFactory(
                        componentContext,
                        config.id,
                        config.order,
                    ),
                )

                is Config.Search -> Search(searchComponentFactory(componentContext, config.query))
                is Config.Ranking -> Ranking(rankingComponentFactory(componentContext))
                is Config.Comment -> Comment(commentComponentFactory(componentContext, config.id))
            }
        }

    override fun navigationToSignIn() {
        navigation.pushNew(Config.SignIn)
    }

    override fun navigationToInterest() {
        navigation.pushNew(Config.Interest)
    }

    override fun navigationToComicList(comics: Comics) {
        navigation.pushNew(Config.ComicList(comics))
    }

    override fun navigationToComicInfo(id: String) {
        navigation.pushNew(Config.ComicInfo(id))
    }

    override fun navigationToReader(id: String, order: Int) {
        navigation.pushNew(Config.Reader(id, order))
    }

    override fun navigationToSearch(query: String?) {
        navigation.pushNew(Config.Search(query))
    }

    override fun navigationToRanking() {
        navigation.pushNew(Config.Ranking)
    }

    override fun navigationToComment(id: String) {
        navigation.pushNew(Config.Comment(id))
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
        data class ComicList(val comics: Comics) : Config

        @Serializable
        data class ComicInfo(val id: String) : Config

        @Serializable
        data class Reader(val id: String, val order: Int) : Config

        @Serializable
        data object Ranking : Config

        @Serializable
        data class Search(val query: String?) : Config

        @Serializable
        data class Comment(val id: String) : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultRootComponent
    }
}
