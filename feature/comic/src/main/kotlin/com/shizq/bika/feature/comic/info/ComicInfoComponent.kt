package com.shizq.bika.feature.comic.info

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.model.ComicResource
import kotlinx.coroutines.flow.StateFlow

interface ComicInfoComponent {
    val comicInfoUiState: StateFlow<ComicInfoUiState>
    fun onClickTrigger(comicResource: ComicResource)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            id: String,
        ): ComicInfoComponent
    }
}
