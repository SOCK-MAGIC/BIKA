package com.shizq.bika.feature.search

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SearchComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
) : SearchComponent, ComponentContext by componentContext {
    @AssistedFactory
    interface Factory : SearchComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): SearchComponentImpl
    }
}
