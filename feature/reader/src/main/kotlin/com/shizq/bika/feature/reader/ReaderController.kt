package com.shizq.bika.feature.reader

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.geometry.Offset
import com.shizq.bika.feature.reader.util.BikaRect
import com.shizq.bika.feature.reader.util.PageScrollingDirection
import com.shizq.bika.feature.reader.util.firstVisibleIndices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface ReaderController {
    val visible: StateFlow<Boolean>
    val title: String
    val onBackPressed: () -> Unit

    val lazyListState: LazyListState

    val progress: StateFlow<Float>
    val hasNextChapter: Boolean
    val hasPreviousChapter: Boolean
    fun updateProgress(v: Float)
    suspend fun onClick(offset: Offset)
}

class BikaReaderController@Inject constructor(
    @ApplicationContext private val appContext: Context,
) : ReaderController {
    override val title: String
        get() = TODO("Not yet implemented")
    override val onBackPressed: () -> Unit
        get() = TODO("Not yet implemented")
    override val visible = MutableStateFlow(false)

    override val progress = MutableStateFlow(1f)
    override val hasNextChapter: Boolean = false
    override val hasPreviousChapter: Boolean = false

    override val lazyListState = LazyListState()

    override fun updateProgress(v: Float) {
        progress.update { v }
    }

    override suspend fun onClick(offset: Offset) {
        val metrics = appContext.resources.displayMetrics
        val scale = Offset(offset.x / metrics.widthPixels, offset.y / metrics.heightPixels)
        onClick(getScrollingDirection(scale))
    }

    suspend fun onClick(direction: PageScrollingDirection) {
        when (direction) {
            PageScrollingDirection.NONE -> visible.update { !visible.value }

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

    private fun hideActionMenu() {
        visible.update { false }
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
}