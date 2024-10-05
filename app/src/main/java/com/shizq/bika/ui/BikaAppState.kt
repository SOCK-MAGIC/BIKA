package com.shizq.bika.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.shizq.bika.core.data.util.NetworkMonitor
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberBikaAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): BikaAppState {
    return remember(
        coroutineScope,
        networkMonitor,
    ) {
        BikaAppState(
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
        )
    }
}

@Stable
class BikaAppState(
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
}
