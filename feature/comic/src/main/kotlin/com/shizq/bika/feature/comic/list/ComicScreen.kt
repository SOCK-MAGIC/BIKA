package com.shizq.bika.feature.comic.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage

@Composable
fun ComicScreen(component: ComicListComponent, navigationToComicInfo: (String) -> Unit) {
    val comicsPagingItems = component.comicFlow.collectAsLazyPagingItems()
    ComicContent(comicsPagingItems, navigationToComicInfo = navigationToComicInfo)
}

@Composable
internal fun ComicContent(
    items: LazyPagingItems<Comic>,
    navigationToComicInfo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold { innerPadding ->
        LazyColumn(modifier = modifier.padding(innerPadding)) {
            items(items.itemCount) { index ->
                items[index]?.let {
                    ComicCard(
                        imageUrl = it.thumbUrl,
                        title = it.title,
                        author = it.author,
                        categories = it.categories,
                        finished = it.finished,
                        epsCount = it.epsCount,
                        pagesCount = it.pagesCount,
                        likeCount = it.likesCount,
                    ) {
                        navigationToComicInfo(it.id)
                    }
                }
            }
        }
    }
}

@Composable
private fun ComicCard(
    imageUrl: String,
    title: String,
    author: String,
    categories: String,
    finished: Boolean,
    epsCount: Int,
    pagesCount: Int,
    likeCount: Int,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Row {
            DynamicAsyncImage(
                imageUrl,
                null,
                Modifier.size(120.dp, 180.dp),
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(IntrinsicSize.Min),
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    color = if (finished) MaterialTheme.colorScheme.primary else Color.Unspecified,
                )
                Text("${epsCount}E/${pagesCount}P", style = MaterialTheme.typography.bodyMedium)
                Text(
                    author,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(categories, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(Icons.Default.Favorite, null, Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(likeCount.toString())
                }
            }
        }
    }
}

@Preview
@Composable
private fun ComicCardPreview() {
    ComicCard(
        imageUrl = "https://www.google.com/#q=unum",
        title = "petentium",
        author = "etiam",
        categories = "tractatos",
        finished = false,
        epsCount = 7864,
        pagesCount = 2924,
        likeCount = 4109,
        onClick = {},
    )
}