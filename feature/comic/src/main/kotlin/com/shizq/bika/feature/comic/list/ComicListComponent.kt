package com.shizq.bika.feature.comic.list

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.model.Comics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ComicListComponent {
    val comicFlow: Flow<PagingData<ComicResource>>

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            comics: Comics,
        ): ComicListComponent
    }
}
