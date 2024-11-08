package com.shizq.bika.feature.reader.util

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import kotlin.math.max

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

val LazyListState.firstVisibleIndices: Int
    get() = if (layoutInfo.visibleItemsInfo.isEmpty()) {
        0
    } else {
        val fullyVisibleItemsInfo = layoutInfo.visibleItemsInfo.toMutableList()

        val firstItemIfLeft = fullyVisibleItemsInfo.firstOrNull()
        if (firstItemIfLeft != null && firstItemIfLeft.offset < layoutInfo.viewportStartOffset) {
            fullyVisibleItemsInfo.removeAt(0)
        }

        fullyVisibleItemsInfo.first().index
    }
