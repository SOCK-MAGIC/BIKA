package com.shizq.bika.feature.reader

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            items(lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let {
                    DynamicAsyncImage(it.url, modifier = Modifier)
                }
            }
        }
    }
}
