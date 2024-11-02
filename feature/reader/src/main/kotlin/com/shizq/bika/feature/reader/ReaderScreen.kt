package com.shizq.bika.feature.reader

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.shizq.bika.core.designsystem.component.ComicReadingAsyncImage
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
        Box(
            Modifier
                .padding(innerPadding)
                .onKeyEvent {
                    when (it.key) {
                        Key.VolumeUp -> {
                            true
                        }

                        Key.VolumeDown -> {
                            true
                        }

                        else -> false
                    }
                }
                .fillMaxSize(),
        ) {
            LazyColumn(state = clickControl.lazyListState, modifier = Modifier.fillMaxSize()) {
                items(lazyPagingItems.itemCount, key = { it }) { index ->
                    lazyPagingItems[index]?.let {
                        ComicReadingAsyncImage(it.url, modifier = Modifier.fillMaxSize())
                    }
                }
            }
            Text(
                clickControl.scrollPosition.toString(),
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}
