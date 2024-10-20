package com.shizq.bika.feature.comic.list

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.model.ComicResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ComicListComponent {
    val comicFlow: Flow<PagingData<ComicResource>>
    val categoryVisibilityUiState: StateFlow<Map<String, Boolean>>
    fun updateCategoryState(name: String, state: Boolean)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            category: String?,
        ): ComicListComponent
    }
}
