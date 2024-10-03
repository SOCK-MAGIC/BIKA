package com.shizq.bika.feature.comic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage

@Composable
fun ComicScreen(component: ComicListComponent) {
    val comicsPagingItems = component.comicFlow.collectAsLazyPagingItems()
    ComicContent(comicsPagingItems)
}

@Composable
internal fun ComicContent(
    items: LazyPagingItems<Comic>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items.itemCount) { index ->
            val doc = items[index]
            doc?.let {
                ComicCard(it.thumbUrl, it.title, it.author, it.categories)
            }
        }
    }
}

@Composable
private fun ComicCard(imageUrl: String, title: String, author: String, categories: String) {
    Card(
        Modifier
            .fillMaxWidth()
    ) {
        Row {
            DynamicAsyncImage(
                imageUrl,
                null,
                Modifier.size(120.dp, 180.dp),
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text("1E305p", style = MaterialTheme.typography.bodyMedium)
                Text(
                    author,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(categories, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.weight(1f))
                Row {
                    Text("1234")
                }
            }
        }
    }
}
