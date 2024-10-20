package com.shizq.bika.feature.comic.info

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface ComicInfoComponent {
    val comicInfoUiState: StateFlow<ComicInfoUiState>
    fun onClickTrigger(id: String)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            id: String,
        ): ComicInfoComponent
    }
}
