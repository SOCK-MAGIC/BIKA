package com.shizq.bika.feature.reader

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListPrefetchStrategy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.datastore.model.Orientation
import com.shizq.bika.feature.reader.util.BikaRect
import com.shizq.bika.feature.reader.util.Debounce
import com.shizq.bika.feature.reader.util.PageScrollingDirection
import com.shizq.bika.feature.reader.util.firstVisibleIndices
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
class ReaderComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted id: String,
    @Assisted order: Int,
    @ApplicationContext private val appContext: Context,
    readerPagingSourceFactory: ReaderPagingSource.Factory,
    private val preferences: BikaPreferencesDataSource,
) : ReaderComponent,
    ComponentContext by componentContext {
    private val debounce = Debounce(MINIMUM_CLICK_INTERVAL)
    override var slideTrack by mutableIntStateOf(1)
    override val currentItemIndex = snapshotFlow { lazyListState.firstVisibleItemIndex.toFloat() }

    override var showActionMenu by mutableStateOf(false)

    override var pageCount by mutableFloatStateOf(1f)

    override val lazyListState =
        LazyListState(prefetchStrategy = LazyListPrefetchStrategy(NESTED_PREFETCH_ITEM_COUNT))

    override val bottomText = MutableStateFlow("")

    override fun updateCurrentItemIndex(scope: CoroutineScope) {
        snapshotFlow { slideTrack }
            .map { lazyListState.scrollToItem(it) }
            .launchIn(scope)
    }

    override fun updateOrientation(orientation: Orientation) {
        componentScope.launch {
            preferences.setOrientation(orientation)
        }
    }

    override val picturePagingFlow = Pager(
        PagingConfig(pageSize = 5, prefetchDistance = 3),
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
        debounce {
            scope.launch {
                Log.d(
                    "ReaderComponent ",
                    "onClick ${lazyListState.firstVisibleIndices} ${lazyListState.firstVisibleItemIndex}",
                )
                when (direction) {
                    PageScrollingDirection.NONE -> showActionMenu = !showActionMenu
                    PageScrollingDirection.PREV -> {
                        if (lazyListState.canScrollBackward) {
                            lazyListState.animateScrollToItem(lazyListState.firstVisibleIndices - 1)
                        }
                        hideActionMenu()
                    }

                    PageScrollingDirection.NEXT -> {
                        if (lazyListState.canScrollForward) {
                            lazyListState.animateScrollToItem(lazyListState.firstVisibleIndices + 1)
                        }
                        hideActionMenu()
                    }
                }
            }
        }
    }

    private fun hideActionMenu() {
        showActionMenu = false
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

private const val NESTED_PREFETCH_ITEM_COUNT = 5
private const val MINIMUM_CLICK_INTERVAL = 200L
