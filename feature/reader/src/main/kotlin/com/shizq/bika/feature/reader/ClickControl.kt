package com.shizq.bika.feature.reader

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
internal fun rememberClickControl(scope: CoroutineScope = rememberCoroutineScope()): ClickControl {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    return remember(scope, density, configuration) {
        ClickControl(
            scope,
            with(density) { configuration.screenWidthDp.dp.roundToPx() },
            with(density) { configuration.screenHeightDp.dp.roundToPx() },
        )
    }
}

@Stable
internal class ClickControl(
    private val scope: CoroutineScope,
    private val width: Int,
    private val height: Int,
) {
    @Stable
    val lazyListState = LazyListState()
    private var scrollPosition = lazyListState.firstVisibleItemIndex
    private fun click(action: Action?) {
        scope.launch {
            when (action) {
                Action.NON -> Unit
                Action.MENU -> Unit
                Action.PREV -> {
                    if (lazyListState.canScrollBackward) {
                        lazyListState.animateScrollToItem(scrollPosition--)
                    }
                }

                Action.NEXT -> {
                    if (lazyListState.canScrollForward) {
                        lazyListState.animateScrollToItem(scrollPosition++)
                    }
                }

                null -> Unit
            }
        }
    }

    fun click(offset: Offset) {
        val scale = Offset(offset.x / width, offset.y / height)
        click(action(scale))
    }

    internal fun action(scale: Offset) = when (scale) {
        in ClickArea.TopStart -> ClickAction.TopStart
        in ClickArea.TopCenter -> ClickAction.TopCenter
        in ClickArea.TopEnd -> ClickAction.TopEnd
        in ClickArea.CenterStart -> ClickAction.CenterStart
        in ClickArea.Center -> ClickAction.Center
        in ClickArea.CenterEnd -> ClickAction.CenterEnd
        in ClickArea.BottomStart -> ClickAction.BottomStart
        in ClickArea.BottomCenter -> ClickAction.BottomCenter
        in ClickArea.BottomEnd -> ClickAction.BottomEnd
        else -> throw UnsupportedOperationException("$scale")
    }

    internal data object ClickArea {
        val TopStart = Rect(0f, 0f, 0.33f, 0.33f)
        val TopCenter = Rect(0.33f, 0f, 0.66f, 0.33f)
        val TopEnd = Rect(0.66f, 0f, 1f, 0.33f)
        val CenterStart = Rect(0f, 0.33f, 0.33f, 0.66f)
        val Center = Rect(0.33f, 0.33f, 0.66f, 0.66f)
        val CenterEnd = Rect(0.66f, 0.33f, 1f, 0.66f)
        val BottomStart = Rect(0f, 0.66f, 0.33f, 1f)
        val BottomCenter = Rect(0.33f, 0.66f, 0.66f, 1f)
        val BottomEnd = Rect(0.66f, 0.66f, 1f, 1f)
    }

    internal data object ClickAction {
        val TopStart = Action.PREV
        val TopCenter = Action.PREV
        val TopEnd = Action.NEXT
        val CenterStart = Action.PREV
        val Center = Action.MENU
        val CenterEnd = Action.NEXT
        val BottomStart = Action.PREV
        val BottomCenter = Action.NEXT
        val BottomEnd = Action.NEXT
    }

    internal enum class Action(val actionName: String) {
        NON("无操作"),
        MENU("菜单"),
        NEXT("下一页"),
        PREV("上一页"),
    }
}

fun LazyListState.visibleItems(itemVisiblePercentThreshold: Float) =
    layoutInfo
        .visibleItemsInfo
        .filter {
            visibilityPercent(it) >= itemVisiblePercentThreshold
        }

fun LazyListState.visibilityPercent(info: LazyListItemInfo): Float {
    val cutTop = max(0, layoutInfo.viewportStartOffset - info.offset)
    val cutBottom = max(0, info.offset + info.size - layoutInfo.viewportEndOffset)

    return max(0f, 100f - (cutTop + cutBottom) * 100f / info.size)
}

val LazyListState.fullyVisibleIndices: List<Int>
    get() = if (layoutInfo.visibleItemsInfo.isEmpty()) {
        emptyList()
    } else {
        val fullyVisibleItemsInfo = layoutInfo.visibleItemsInfo.toMutableList()

        val lastItem = fullyVisibleItemsInfo.last()

        val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

        if (lastItem.offset + lastItem.size > viewportHeight) {
            fullyVisibleItemsInfo.removeAt(fullyVisibleItemsInfo.lastIndex)
        }

        val firstItemIfLeft = fullyVisibleItemsInfo.firstOrNull()
        if (firstItemIfLeft != null && firstItemIfLeft.offset < layoutInfo.viewportStartOffset) {
            fullyVisibleItemsInfo.removeAt(0)
        }

        fullyVisibleItemsInfo.map { it.index }
    }
