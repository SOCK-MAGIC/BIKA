package com.shizq.bika.feature.reader

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.model.Picture
import kotlinx.coroutines.flow.Flow

interface ReaderComponent {
    val pictureFlow: Flow<PagingData<Picture>>
    val pageCount: Float

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            id: String,
            order: Int,
        ): ReaderComponent
    }
}
