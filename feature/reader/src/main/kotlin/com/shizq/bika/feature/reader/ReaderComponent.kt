package com.shizq.bika.feature.reader

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.model.Picture
import kotlinx.coroutines.flow.Flow

interface ReaderComponent {
    val picturePagingFlow: Flow<PagingData<Picture>>
    val controller: ReaderController

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            id: String,
            order: Int,
        ): ReaderComponent
    }
}
