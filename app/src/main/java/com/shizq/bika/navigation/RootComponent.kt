package com.shizq.bika.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.shizq.bika.feature.comic.info.ComicInfoComponent
import com.shizq.bika.feature.comic.list.ComicListComponent
import com.shizq.bika.feature.interest.InterestComponent
import com.shizq.bika.feature.ranking.RankingComponent
import com.shizq.bika.feature.reader.ReaderComponent
import com.shizq.bika.feature.search.SearchComponent
import com.shizq.bika.feature.signin.SignInComponent
import com.shizq.bika.feature.splash.SplashComponent
import com.shizq.bika.router.ChildDrawer

interface RootComponent : ComponentContext {
    val stack: Value<ChildStack<*, Child>>
    val drawer: Value<ChildDrawer<DrawerComponent>>
    fun navigationToSignIn()
    fun navigationToInterest()
    fun navigationToComicList(category: String?)
    fun navigationToComicInfo(id: String)
    fun navigationToReader(id: String)
    fun navigationToSearch()
    fun navigationToRanking()
    fun onBack()
    fun setDrawerState(isOpen: Boolean)
    sealed class Child {
        data class SignIn(val component: SignInComponent) : Child()
        data class Interest(val component: InterestComponent) : Child()
        data class ComicList(val component: ComicListComponent) : Child()
        data class ComicInfo(val component: ComicInfoComponent) : Child()
        data class Splash(val component: SplashComponent) : Child()
        data class Reader(val component: ReaderComponent) : Child()
        data class Search(val component: SearchComponent) : Child()
        data class Ranking(val component: RankingComponent) : Child()
    }

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): RootComponent
    }
}
