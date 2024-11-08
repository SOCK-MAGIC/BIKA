package com.shizq.bika.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.shizq.bika.core.data.util.ErrorMessage
import com.shizq.bika.core.data.util.MessageDuration
import com.shizq.bika.core.network.model.Comics
import com.shizq.bika.core.ui.LocalAnimatedVisibilityScope
import com.shizq.bika.core.ui.LocalSharedTransitionScope
import com.shizq.bika.feature.comic.info.ComicInfoScreen
import com.shizq.bika.feature.comic.list.ComicScreen
import com.shizq.bika.feature.comment.CommentScreen
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
    val snackbarHostState = remember { SnackbarHostState() }

    SideEffect {
        appState.offlineMessage = "⚠\uFE0F You aren’t connected to the internet"
    }

    val snackbarMessage by appState.snackbarMessage.collectAsStateWithLifecycle()

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            val snackBarResult = snackbarHostState.showSnackbar(
                message = it.message,
                actionLabel = it.label,
                duration = snackbarDurationOf(it.duration),
            ) == ActionPerformed

            handleSnackbarResult(snackBarResult, it)
            // Remove Message from Queue
            appState.clearErrorMessage(it.id)
        }
    }

    BikaApp(
        component = component,
        snackbarHostState = snackbarHostState,
        windowAdaptiveInfo = windowAdaptiveInfo,
    )
}

@Composable
internal fun BikaApp(
    component: RootComponent,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.semantics {
            testTagsAsResourceId = true
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {}
        Box(modifier = Modifier.consumeWindowInsets(WindowInsets(0, 0, 0, 0))) {
            val drawerState = rememberNavigationDrawerState(
                drawer = component.drawer,
                onStateChanged = component::setDrawerState,
            )
            ModalNavigationDrawer(
                drawerContent = {
                    DrawerScreen(
                        component = drawerState.instance,
                        drawerState = drawerState.drawerState,
                        navigationToComicList = {
                            component.navigationToComicList(Comics(it))
                            component.setDrawerState(false)
                        },
                        modifier = Modifier,
                    )
                },
                gesturesEnabled = false,
                drawerState = drawerState.drawerState,
            ) {
                RootContent(component)
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    SharedTransitionLayout(modifier = modifier) {
        ChildStack(
            stack = component.stack,
            modifier = modifier,
            animation = stackAnimation { child, otherChild, direction ->
                when (child.instance) {
                    is RootComponent.Child.ComicList,
                    is RootComponent.Child.ComicInfo,
                    -> null

                    else -> slide() + fade()
                }
            },
        ) { created ->
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this@SharedTransitionLayout,
                LocalAnimatedVisibilityScope provides this,
            ) {
                when (val child = created.instance) {
                    is RootComponent.Child.Splash -> SplashScreen(
                        component = child.component,
                        component::navigationToInterest,
                        component::navigationToSignIn,
                    )

                    is RootComponent.Child.SignIn -> SignInScreen(
                        component = child.component,
                    ) {
                        component.onBack()
                        component.navigationToInterest()
                    }

                    is RootComponent.Child.Interest -> InterestScreen(
                        component = child.component,
                        navigationToComicList = {
                            component.navigationToComicList(Comics(it))
                        },
                        navigationToSearch = component::navigationToSearch,
                        navigationToRanking = component::navigationToRanking,
                        openDrawer = { component.setDrawerState(true) },
                    )

                    is RootComponent.Child.ComicList -> ComicScreen(
                        component = child.component,
                        navigationToComicInfo = component::navigationToComicInfo,
                    )

                    is RootComponent.Child.ComicInfo -> ComicInfoScreen(
                        component = child.component,
                        navigationToReader = component::navigationToReader,
                        navigationToComicList = component::navigationToComicList,
                        navigationToComment = component::navigationToComment,
                        navigationToComicInfo = component::navigationToComicInfo,
                    )

                    is RootComponent.Child.Reader -> ReaderScreen(component = child.component)
                    is RootComponent.Child.Ranking -> RankingScreen(
                        component = child.component,
                        navigationToComicInfo = component::navigationToComicInfo,
                        navigationToSearch = {
                            component.navigationToComicList(Comics(creatorId = it))
                        },
                    )

                    is RootComponent.Child.Search -> SearchScreen(
                        component = child.component,
                        onBackClick = component::onBack,
                        navigationToComicInfo = component::navigationToComicInfo,
                    )

                    is RootComponent.Child.Comment -> CommentScreen(
                        component = child.component,
                        onBackClick = component::onBack,
                    )
                }
            }
        }
    }
}

private fun snackbarDurationOf(duration: MessageDuration?): SnackbarDuration {
    return when (duration) {
        MessageDuration.Short -> SnackbarDuration.Short
        MessageDuration.Long -> SnackbarDuration.Long
        MessageDuration.Indefinite -> SnackbarDuration.Indefinite
        else -> SnackbarDuration.Short
    }
}

private fun handleSnackbarResult(snackBarResult: Boolean, message: ErrorMessage) {
    if (snackBarResult) {
        message.actionPerformed?.invoke()
    } else {
        message.actionNotPerformed?.invoke()
    }
}
