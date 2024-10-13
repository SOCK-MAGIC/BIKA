package com.shizq.bika.core.ui

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.shizq.bika.core.model.ComicResource

fun LazyListScope.comicCardItems(
    items: List<ComicResource>,
    onComicClick: (String) -> Unit,
    itemModifier: Modifier = Modifier,
) = items(
    items = items,
    key = { it.id },
    itemContent = { comic ->
        ComicCard(comic, itemModifier) {
            onComicClick(comic.id)
        }
    },
)

fun LazyListScope.comicCardItems(
    lazyPagingItems: LazyPagingItems<ComicResource>,
    itemModifier: Modifier = Modifier,
    onComicClick: (String) -> Unit,
) = items(lazyPagingItems.itemCount, { it }) {
    lazyPagingItems[it]?.let { resource ->
        ComicCard(resource, itemModifier) {
            onComicClick(resource.id)
        }
    }
}
