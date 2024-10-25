package com.shizq.bika.feature.comic.list

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.data.model.PagingMetadata
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.model.Comics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ComicListComponent {
    val comicFlow: Flow<PagingData<ComicResource>>
    val categoryVisibilityUiState: StateFlow<Map<String, Boolean>>
    val pageMetadata: PagingMetadata?
    var page: Int
    fun updateCategoryState(name: String, state: Boolean)

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            comics: Comics,
        ): ComicListComponent
    }
}
