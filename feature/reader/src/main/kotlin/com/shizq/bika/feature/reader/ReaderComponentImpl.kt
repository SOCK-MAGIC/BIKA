package com.shizq.bika.feature.reader

import androidx.collection.mutableFloatSetOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.data.model.PagingMetadata
import com.shizq.bika.core.data.paging.ReaderPagingSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ReaderComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted id: String,
    @Assisted order: Int,
    readerPagingSourceFactory: ReaderPagingSource.Factory,
) : ReaderComponent,
    ComponentContext by componentContext {

    override var pageCount by mutableFloatStateOf(1f)
    override val pictureFlow = Pager(
        PagingConfig(pageSize = 40),
    ) {
        readerPagingSourceFactory(id, order) {
            pageCount = it.totalPages.toFloat()
        }
    }
        .flow
        .cachedIn(componentScope)

    @AssistedFactory
    interface Factory : ReaderComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            id: String,
            order: Int,
        ): ReaderComponentImpl
    }
}
