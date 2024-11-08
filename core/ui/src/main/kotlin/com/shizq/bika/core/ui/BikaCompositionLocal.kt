package com.shizq.bika.core.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

val LocalAnimatedVisibilityScope =
    staticCompositionLocalOf<AnimatedVisibilityScope> { noLocalProvidedFor("LocalAnimatedVisibilityScope") }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope =
    staticCompositionLocalOf<SharedTransitionScope> { noLocalProvidedFor("LocalSharedTransitionScope") }

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}

fun NavGraphBuilder.composableWithCompositionLocal(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (
    @JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
    )? = { fadeIn() },
    exitTransition: (
    @JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
    )? = { fadeOut() },
    popEnterTransition: (
    @JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
    )? = enterTransition,
    popExitTransition: (
    @JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
    )? = exitTransition,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route,
        arguments,
        deepLinks,
        enterTransition,
        exitTransition,
        popEnterTransition,
        popExitTransition,
    ) {
        CompositionLocalProvider(
            LocalAnimatedVisibilityScope provides this@composable,
        ) {
            content(it)
        }
    }
}

fun <T> spatialExpressiveSpring() = spring<T>(
    dampingRatio = 0.8f,
    stiffness = 380f,
)

fun <T> nonSpatialExpressiveSpring() = spring<T>(
    dampingRatio = 1f,
    stiffness = 1600f,
)

@OptIn(ExperimentalSharedTransitionApi::class)
val BikaDetailBoundsTransform = BoundsTransform { _, _ ->
    spatialExpressiveSpring()
}
