package com.shizq.bika.feature.reader

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ReaderComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted id: String,
    private val network: BikaNetworkDataSource,
) : ReaderComponent, ComponentContext by componentContext {
    override val pictureFlow = Pager(
        PagingConfig(pageSize = 40),
    ) { ReaderPagingSource(network, id) }
        .flow
        .cachedIn(componentScope)

    @AssistedFactory
    interface Factory : ReaderComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            id: String,
        ): ReaderComponentImpl
    }
}
