package com.shizq.bika.feature.comic.list

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ComicListComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted category: String,
    private val network: BikaNetworkDataSource,
) : ComicListComponent, ComponentContext by componentContext {
    override val comicFlow = Pager(
        PagingConfig(pageSize = 20)
    ) { ComicListPagingSource(network, category) }
        .flow
        .cachedIn(componentScope)

    @AssistedFactory
    interface Factory : ComicListComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            category: String,
        ): ComicListComponentImpl
    }
}

sealed interface ComicListUiState