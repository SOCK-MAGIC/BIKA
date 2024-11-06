package com.shizq.bika.feature.reader

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.geometry.Offset
import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.model.Picture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ReaderComponent {
    val picturePagingFlow: Flow<PagingData<Picture>>
    val pageCount: Float

    fun onClick(offset: Offset, scope: CoroutineScope)
    fun onClick(direction: PageScrollingDirection, scope: CoroutineScope)

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            id: String,
            order: Int,
        ): ReaderComponent
    }

    val bottomText: StateFlow<String>
    val lazyListState: LazyListState
    val showActionMenu: Boolean
}
