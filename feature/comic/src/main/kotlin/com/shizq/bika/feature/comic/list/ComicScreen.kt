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
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.HideSource
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.model.Sort
import com.shizq.bika.core.ui.comicCardItems

@Composable
fun ComicScreen(component: ComicListComponent, navigationToComicInfo: (String) -> Unit) {
    val comicsPagingItems = component.comicFlow.collectAsLazyPagingItems()
    var showSealTagDialog by rememberSaveable { mutableStateOf(false) }
    var showSortDialog by rememberSaveable { mutableStateOf(false) }
    ComicContent(
        comicsPagingItems,
        showSealTagDialog = showSealTagDialog,
        onDismissed = {
            showSealTagDialog = false
            showSortDialog = false
        },
        onTopAppBarActionClick = { showSealTagDialog = true },
        showSortDialog = showSortDialog,
        onDismissedSortDialog = { showSortDialog = false },
        onActionSortDialogClick = { showSortDialog = true },
        navigationToComicInfo = navigationToComicInfo,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ComicContent(
    lazyPagingItems: LazyPagingItems<ComicResource>,
    navigationToComicInfo: (String) -> Unit,
    showSealTagDialog: Boolean,
    onDismissed: () -> Unit,
    onTopAppBarActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    showSortDialog: Boolean,
    onDismissedSortDialog: () -> Unit,
    onActionSortDialogClick: () -> Unit,
) {
    if (showSealTagDialog) {
        SettingsDialog { onDismissed() }
    }
    if (showSortDialog) {
        SortDialog { onDismissed() }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                {},
                actions = {
                    IconButton(onActionSortDialogClick) {
                        Icon(Icons.AutoMirrored.Rounded.Sort, "sort")
                    }
                    IconButton(onTopAppBarActionClick) {
                        Icon(Icons.Rounded.HideSource, "hide tag")
                    }
                    Text("1/1")
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(modifier = modifier.padding(innerPadding)) {
            comicCardItems(lazyPagingItems) {
                navigationToComicInfo(it)
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
