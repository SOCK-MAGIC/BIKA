package com.shizq.bika.feature.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
) : SearchComponent,
    ComponentContext by componentContext {

    private val searchQueryHandle = instanceKeeper.getOrCreateSimple { MutableStateFlow("") }

    override val searchQuery = searchQueryHandle.asStateFlow()
    override fun onSearchQueryChanged(query: String) {
        searchQueryHandle.value = query
    }

    override fun onSearchTriggered(query: String) {
        componentScope.launch {
            network.advanced_search(query, 1)
        }
    }

    @AssistedFactory
    interface Factory : SearchComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): SearchComponentImpl
    }
}
