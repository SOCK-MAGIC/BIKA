package com.shizq.bika.feature.reader

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow

interface ReaderComponent {
    val pictureFlow: Flow<PagingData<Picture>>

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            id: String,
        ): ReaderComponent
    }
}
