package com.shizq.bika.feature.reader

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.data.paging.ReaderPagingSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@OptIn(ExperimentalFoundationApi::class)
class ReaderComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted id: String,
    @Assisted order: Int,
    readerPagingSourceFactory: ReaderPagingSource.Factory,
    override val controller: ReaderController,
) : ReaderComponent,
    ComponentContext by componentContext {
    override val picturePagingFlow = Pager(
        PagingConfig(pageSize = 20, prefetchDistance = 3),
    ) {
        readerPagingSourceFactory(id, order) {
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
