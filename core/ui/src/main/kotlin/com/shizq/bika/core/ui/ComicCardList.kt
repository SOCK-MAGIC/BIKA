package com.shizq.bika.core.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.shizq.bika.core.model.ComicResource

fun LazyListScope.comicCardItems(
    resourceList: List<ComicResource>,
    itemModifier: Modifier = Modifier,
    onComicClick: (String) -> Unit,
) = items(resourceList, { it.id }) { item ->
    ComicCard(item, itemModifier) {
        onComicClick(item.id)
    }
}

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

@OptIn(ExperimentalSharedTransitionApi::class)
fun LazyListScope.comicCardItems(
    lazyPagingItems: LazyPagingItems<ComicResource>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    itemModifier: Modifier = Modifier,
    onComicClick: (String) -> Unit,
) = items(lazyPagingItems.itemCount, { it }) {
    lazyPagingItems[it]?.let { resource ->
        ComicCard(resource, itemModifier) {
            onComicClick(resource.id)
        }
    }
}
