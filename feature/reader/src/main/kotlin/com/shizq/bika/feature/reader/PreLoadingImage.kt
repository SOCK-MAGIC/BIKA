package com.shizq.bika.feature.reader

import androidx.compose.foundation.lazy.LazyListState

internal val LazyListState.lastItemIndex: Int?
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index
