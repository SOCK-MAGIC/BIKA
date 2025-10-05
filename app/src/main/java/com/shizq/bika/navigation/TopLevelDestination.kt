package com.shizq.bika.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.shizq.bika.core.navigation.BikaNavKey
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val key: BikaNavKey,
) {

}

internal val TopLevelDestinations = TopLevelDestination.entries.associateBy { dest -> dest.key }