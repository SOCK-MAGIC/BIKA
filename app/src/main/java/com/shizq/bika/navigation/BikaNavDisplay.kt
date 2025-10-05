package com.shizq.bika.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.shizq.bika.core.navigation.BikaBackStack
import com.shizq.bika.core.navigation.BikaNavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BikaNavDisplay(
    bikaBackStack: BikaBackStack,
    entryProviderBuilders: Set<EntryProviderBuilder<BikaNavKey>.() -> Unit>,
) {
    val listDetailStrategy = rememberListDetailSceneStrategy<BikaNavKey>()

    NavDisplay(
        backStack = bikaBackStack.backStack,
        sceneStrategy = listDetailStrategy,
        onBack = { count -> bikaBackStack.popLast(count) },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entryProviderBuilders.forEach { builder ->
                builder()
            }
        },
    )
}