package com.shizq.bika.feature.reader

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage
import com.shizq.bika.core.designsystem.util.PreLoading
import com.shizq.bika.core.model.Picture

@Composable
fun ReaderScreen(
    component: ReaderComponent,
) {
    val items = component.pictureFlow.collectAsLazyPagingItems()
    ReaderContent(items)
}

@Composable
fun ReaderContent(
    lazyPagingItems: LazyPagingItems<Picture>,
) {
    val clickControl = rememberClickControl()
    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    clickControl.click(it)
                }
            }
            .fillMaxSize(),
    ) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding), state = clickControl.lazyListState) {
            items(lazyPagingItems.itemCount, key = { it }) { index ->
                lazyPagingItems[index]?.let {
                    DynamicAsyncImage(it.url, modifier = Modifier)
                }
            }
        }
    }
}
