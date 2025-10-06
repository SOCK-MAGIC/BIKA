package com.shizq.bika.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.shizq.bika.core.data.util.ErrorMessage
import com.shizq.bika.core.data.util.ErrorMonitor
import com.shizq.bika.core.data.util.NetworkMonitor
import com.shizq.bika.core.navigation.BikaBackStack
import com.shizq.bika.navigation.TopLevelDestination
import com.shizq.bika.navigation.TopLevelDestinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberBikaAppState(
    networkMonitor: NetworkMonitor,
    errorMonitor: ErrorMonitor,
    bikaBackStack: BikaBackStack,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): BikaAppState {
    return remember(
        coroutineScope,
        networkMonitor,
        errorMonitor,
        bikaBackStack,
    ) {
        BikaAppState(
            bikaBackStack = bikaBackStack,
            coroutineScope = coroutineScope,
            errorMonitor = errorMonitor,
        )
    }
}

@Stable
class BikaAppState(
    val bikaBackStack: BikaBackStack,
    coroutineScope: CoroutineScope,
    errorMonitor: ErrorMonitor,
) : ErrorMonitor by errorMonitor {
    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = TopLevelDestinations[bikaBackStack.currentTopLevelKey]
    val isOfflineState: StateFlow<Boolean> = isOffline.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false,
    )

    val snackbarMessage: StateFlow<ErrorMessage?> = errorMessage.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries
    fun navigateToTopLevelDestination(destination: TopLevelDestination) {

    }
}
