package com.shizq.bika.core.designsystem.util

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage

// rename
@Composable
fun PreLoading(
    dataSize: Int,
    dataGetter: (Int) -> String,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val preloadingData = rememberCoilPreloadingData(
        dataSize,
        dataGetter,
    ) {
        data(it).build()
    }
    LazyColumn(modifier, state = lazyListState) {
        items(preloadingData.size, key = { it }) { index ->
            AsyncImage(preloadingData[index].second, null, Modifier)
        }
    }
}
