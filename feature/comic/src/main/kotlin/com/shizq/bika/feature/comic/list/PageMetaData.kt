package com.shizq.bika.feature.comic.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.shizq.bika.core.data.model.PagingMetadata
import kotlinx.coroutines.flow.distinctUntilChanged

object PageMetaData {
    var metadata by mutableStateOf<PagingMetadata?>(null)
    var redirectedPage by mutableStateOf<Int?>(null)
    val redirectedPageFlow = snapshotFlow { redirectedPage }.distinctUntilChanged()
}
