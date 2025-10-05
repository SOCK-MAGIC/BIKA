package com.shizq.bika.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.ExploreOff
import androidx.compose.ui.graphics.vector.ImageVector
import com.shizq.bika.core.navigation.BikaNavKey
import com.shizq.bika.router.InterestsRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val route: KClass<*>,
    val key: BikaNavKey,
) {
    Interests(
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Filled.ExploreOff,
        iconText = "探索",
        route = InterestsRoute::class,
        key = InterestsRoute,
    )
}

internal val TopLevelDestinations = TopLevelDestination.entries.associateBy { dest -> dest.key }