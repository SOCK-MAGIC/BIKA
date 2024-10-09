package com.shizq.bika.feature.reader

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage

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
        LazyColumn(Modifier.padding(innerPadding), state = clickControl.lazyListState,) {
            items(lazyPagingItems.itemCount, key = { it }) { index ->
                lazyPagingItems[index]?.let {
                    DynamicAsyncImage(it.url, modifier = Modifier)
                }
            }
        }
    }
}

@Preview
@Composable
private fun TestPointerInputPreview() {
    TestPointerInput()
}

@Composable
fun TestPointerInput() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            items(100) { index ->
                Text(
                    "$index",
                    Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    fontSize = 16.sp,
                )
            }
        }
    }
}
