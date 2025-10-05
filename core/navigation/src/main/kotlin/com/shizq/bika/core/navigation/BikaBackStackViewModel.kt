package com.shizq.bika.core.navigation

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.savedstate.serialization.SavedStateConfiguration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import javax.inject.Inject

@HiltViewModel
class BikaBackStackViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val bikaBackStack: BikaBackStack,
    serializersModules: SerializersModule,
) : ViewModel() {

    private val config = SavedStateConfiguration { serializersModule = serializersModules }

    @VisibleForTesting
    internal var backStackMap by savedStateHandle.saved(
        serializer = getMapSerializer<BikaNavKey>(),
        configuration = config,
    ) {
        linkedMapOf()
    }

    init {
        if (backStackMap.isNotEmpty()) {
            // Restore backstack from saved state handle if not emtpy
            @Suppress("UNCHECKED_CAST")
            bikaBackStack.restore(
                backStackMap as LinkedHashMap<BikaNavKey, MutableList<BikaNavKey>>,
            )
        }

        // Start observing changes to the backStack and save backStack whenever it updates
        viewModelScope.launch {
            snapshotFlow {
                bikaBackStack.backStack.toList()
                backStackMap = bikaBackStack.backStackMap
            }.collect()
        }
    }
}

private inline fun <reified T : BikaNavKey> getMapSerializer() =
    MapSerializer(serializer<T>(), serializer<List<T>>())