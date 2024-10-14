package com.shizq.bika.feature.comic.list

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.model.ComicResource
import kotlinx.coroutines.flow.Flow

interface ComicListComponent {
    val comicFlow: Flow<PagingData<ComicResource>>

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            category: String,
        ): ComicListComponent
    }
}
