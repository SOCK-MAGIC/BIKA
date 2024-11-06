package com.shizq.bika.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.shizq.bika.core.data.util.ErrorMessage
import com.shizq.bika.core.data.util.ErrorMonitor
import com.shizq.bika.core.data.util.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberBikaAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    errorMonitor: ErrorMonitor,
): BikaAppState {
    return remember(
        coroutineScope,
        networkMonitor,
        errorMonitor,
    ) {
        BikaAppState(
            coroutineScope = coroutineScope,
            errorMonitor = errorMonitor,
        )
    }
}

@Stable
class BikaAppState(
    coroutineScope: CoroutineScope,
    errorMonitor: ErrorMonitor,
) : ErrorMonitor by errorMonitor {
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
}
