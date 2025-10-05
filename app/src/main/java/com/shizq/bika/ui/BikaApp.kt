package com.shizq.bika.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderBuilder
import com.shizq.bika.core.data.util.ErrorMessage
import com.shizq.bika.core.data.util.MessageDuration
import com.shizq.bika.core.designsystem.component.BikaBackground
import com.shizq.bika.core.designsystem.component.BikaGradientBackground
import com.shizq.bika.core.designsystem.component.BikaNavigationSuiteScaffold
import com.shizq.bika.core.navigation.BikaNavKey
import com.shizq.bika.navigation.BikaNavDisplay

@Composable
fun BikaApp(
    appState: BikaAppState,
    entryProviderBuilders: Set<EntryProviderBuilder<BikaNavKey>.() -> Unit>,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    BikaBackground(modifier = modifier) {
        BikaGradientBackground {
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
                appState = appState,
                entryProviderBuilders = entryProviderBuilders,
            )
        }
    }
}

@Composable
internal fun BikaApp(
    appState: BikaAppState,
    entryProviderBuilders: Set<EntryProviderBuilder<BikaNavKey>.() -> Unit>,
    modifier: Modifier = Modifier,
) {
    val currentTopLevelKey = appState.currentTopLevelDestination!!.key

    BikaNavigationSuiteScaffold(
        navigationSuiteItems = {
            appState.topLevelDestinations.forEach { destination ->
                val selected = destination.key == currentTopLevelKey
                item(
                    selected = selected,
                    onClick = { /*appState.navigateToTopLevelDestination(destination)*/ },
                    icon = {
                        Icon(
                            imageVector = destination.unselectedIcon,
                            contentDescription = null,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = destination.selectedIcon,
                            contentDescription = null,
                        )
                    },
                    label = { Text(stringResource(destination.iconTextId)) },
                    modifier = Modifier
                        .testTag("BikaNavItem"),
                )
            }
        },
        modifier = modifier,
    ) {
        BikaNavDisplay(
            bikaBackStack = appState.bikaBackStack,
            entryProviderBuilders,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun RootContent(modifier: Modifier = Modifier) {
//    SharedTransitionLayout(modifier = modifier) {
//        ChildStack(
//            stack = component.stack,
//            modifier = modifier,
//            animation = stackAnimation { child, otherChild, direction, isPredictiveBack ->
//                when (child.instance) {
//                    is RootComponent.Child.ComicList,
//                    is RootComponent.Child.ComicInfo,
//                    -> null
//
//                    else -> slide() + fade()
//                }
//            },
//        ) { created ->
//            CompositionLocalProvider(
//                LocalSharedTransitionScope provides this@SharedTransitionLayout,
//                LocalAnimatedVisibilityScope provides this,
//            ) {
//                when (val child = created.instance) {
//                    is RootComponent.Child.Splash -> SplashScreen(
//                        splashViewModel = child.component,
//                        component::navigationToInterest,
//                        component::navigationToSignIn,
//                    )
//
//                    is RootComponent.Child.SignIn -> SignInScreen(
//                        signInViewModel = child.component,
//                    )
//
//                    is RootComponent.Child.Interest -> InterestScreen(
//                        interestViewModel = child.component,
//                        navigationToComicList = {
//                            component.navigationToComicList(Comics(it))
//                        },
//                        navigationToSearch = component::navigationToSearch,
//                        navigationToRanking = component::navigationToRanking,
//                        openDrawer = { component.setDrawerState(true) },
//                    )
//
//                    is RootComponent.Child.ComicList -> ComicScreen(
//                        comicListViewModel = child.component,
//                        navigationToComicInfo = component::navigationToComicInfo,
//                    )
//
//                    is RootComponent.Child.ComicInfo -> ComicInfoScreen(
//                        component = child.component,
//                        navigationToReader = component::navigationToReader,
//                        navigationToComicList = component::navigationToComicList,
//                        navigationToComment = component::navigationToComment,
//                        navigationToComicInfo = component::navigationToComicInfo,
//                    )
//
//                    is RootComponent.Child.Reader -> ReaderScreen(component = child.component)
//                    is RootComponent.Child.Ranking -> RankingScreen(
//                        component = child.component,
//                        navigationToComicInfo = component::navigationToComicInfo,
//                        navigationToSearch = {
//                            component.navigationToComicList(Comics(creatorId = it))
//                        },
//                    )
//
//                    is RootComponent.Child.Search -> SearchScreen(
//                        searchViewModel = child.component,
//                        onBackClick = component::onBack,
//                        navigationToComicInfo = component::navigationToComicInfo,
//                    )
//
//                    is RootComponent.Child.Comment -> CommentScreen(
//                        component = child.component,
//                        onBackClick = component::onBack,
//                    )
//                }
//            }
//        }
//    }
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