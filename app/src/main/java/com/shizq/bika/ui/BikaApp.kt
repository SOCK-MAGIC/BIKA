package com.shizq.bika.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.shizq.bika.feature.comic.info.ComicInfoScreen
import com.shizq.bika.feature.comic.list.ComicScreen
import com.shizq.bika.feature.interest.InterestScreen
import com.shizq.bika.feature.ranking.RankingScreen
import com.shizq.bika.feature.reader.ReaderScreen
import com.shizq.bika.feature.search.SearchScreen
import com.shizq.bika.feature.signin.SignInScreen
import com.shizq.bika.feature.splash.SplashScreen
import com.shizq.bika.navigation.RootComponent
import com.shizq.bika.router.rememberNavigationDrawerState

@Composable
fun BikaApp(
    component: RootComponent,
    appState: BikaAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    BikaApp(component, modifier)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun BikaApp(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    val drawerState = rememberNavigationDrawerState(
        drawer = component.drawer,
        onStateChanged = component::setDrawerState,
    )
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.semantics {
            testTagsAsResourceId = true
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {
            Box(modifier = Modifier.consumeWindowInsets(WindowInsets(0, 0, 0, 0))) {
                RootContent(component)
            }
        }
    }
}

@Composable
private fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation { _ -> slide() + fade() },
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Splash -> SplashScreen(
                component = child.component,
                component::navigationToInterest,
                component::navigationToSignIn,
            )

            is RootComponent.Child.SignIn -> SignInScreen(
                component = child.component,
                component::navigationToInterest,
            )

            is RootComponent.Child.Interest -> InterestScreen(
                component = child.component,
                navigationToComicList = component::navigationToComicList,
                navigationToSearch = component::navigationToSearch,
                navigationToRanking = component::navigationToRanking,
            )

            is RootComponent.Child.ComicList -> ComicScreen(
                component = child.component,
                navigationToComicInfo = component::navigationToComicInfo,
            )

            is RootComponent.Child.ComicInfo -> ComicInfoScreen(
                component = child.component,
                navigationToReader = component::navigationToReader,
            )

            is RootComponent.Child.Reader -> ReaderScreen(component = child.component)
            is RootComponent.Child.Ranking -> RankingScreen(component = child.component)
            is RootComponent.Child.Search -> SearchScreen(
                component = child.component,
                onBackClick = component::onBack,
            )
        }
    }
}
