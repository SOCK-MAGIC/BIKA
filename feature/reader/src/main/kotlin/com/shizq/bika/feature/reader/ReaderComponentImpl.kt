package com.shizq.bika.feature.reader

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListPrefetchStrategy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.data.paging.ReaderPagingSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
class ReaderComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted id: String,
    @Assisted order: Int,
    readerPagingSourceFactory: ReaderPagingSource.Factory,
    @ApplicationContext private val appContext: Context,
) : ReaderComponent,
    ComponentContext by componentContext {
    private val currentItemIndex by derivedStateOf { lazyListState.firstVisibleItemIndex }

    override var showActionMenu by mutableStateOf(false)

    override var pageCount by mutableFloatStateOf(1f)

    override val lazyListState =
        LazyListState(prefetchStrategy = LazyListPrefetchStrategy(NESTED_PREFETCH_ITEM_COUNT))

    override var bottomText = snapshotFlow { "$currentItemIndex/${pageCount.toInt()}" }
        .stateIn(
            componentScope,
            SharingStarted.WhileSubscribed(5000),
            "",
        )

    override val picturePagingFlow = Pager(
        PagingConfig(pageSize = 40),
    ) {
        readerPagingSourceFactory(id, order) {
            pageCount = it.totalPages.toFloat()
        }
    }
        .flow
        .cachedIn(componentScope)

    override fun onClick(offset: Offset, scope: CoroutineScope) {
        val metrics = appContext.resources.displayMetrics
        val scale = Offset(offset.x / metrics.widthPixels, offset.y / metrics.heightPixels)
        onClick(getScrollingDirection(scale), scope)
    }

    override fun onClick(
        direction: PageScrollingDirection,
        scope: CoroutineScope,
    ) {
        scope.launch {
            when (direction) {
                PageScrollingDirection.NONE -> showActionMenu = !showActionMenu
                PageScrollingDirection.PREV -> {
                    if (lazyListState.canScrollBackward) {
                        lazyListState.animateScrollToItem(currentItemIndex - 1)
                    }
                }

                PageScrollingDirection.NEXT -> {
                    if (lazyListState.canScrollForward) {
                        lazyListState.animateScrollToItem(currentItemIndex + 1)
                    }
                }
            }
        }
    }

    private fun getScrollingDirection(offset: Offset) = when (offset) {
        in BikaRect.TopStart -> PageScrollingDirection.PREV
        in BikaRect.TopCenter -> PageScrollingDirection.PREV
        in BikaRect.TopEnd -> PageScrollingDirection.NEXT
        in BikaRect.CenterStart -> PageScrollingDirection.PREV
        in BikaRect.Center -> PageScrollingDirection.NONE
        in BikaRect.CenterEnd -> PageScrollingDirection.NEXT
        in BikaRect.BottomStart -> PageScrollingDirection.PREV
        in BikaRect.BottomCenter -> PageScrollingDirection.NEXT
        in BikaRect.BottomEnd -> PageScrollingDirection.NEXT
        else -> PageScrollingDirection.NONE
    }

    @AssistedFactory
    interface Factory : ReaderComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            id: String,
            order: Int,
        ): ReaderComponentImpl
    }
}

private const val NESTED_PREFETCH_ITEM_COUNT = 20
