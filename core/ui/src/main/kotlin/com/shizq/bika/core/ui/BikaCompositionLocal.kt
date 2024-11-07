package com.shizq.bika.core.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAnimatedVisibilityScope =
    staticCompositionLocalOf<AnimatedVisibilityScope> { noLocalProvidedFor("LocalAnimatedVisibilityScope") }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope =
    staticCompositionLocalOf<SharedTransitionScope> { noLocalProvidedFor("LocalSharedTransitionScope") }

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
