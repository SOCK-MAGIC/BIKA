package com.shizq.bika.feature.comic

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow

interface ComicListComponent {
    val comicFlow: Flow<PagingData<Comic>>

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            category: String,
        ): ComicListComponent
    }
}
