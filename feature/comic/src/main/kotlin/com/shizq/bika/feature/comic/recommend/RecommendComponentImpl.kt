package com.shizq.bika.feature.comic.recommend

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class RecommendComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
) : RecommendComponent, ComponentContext by componentContext {
    @AssistedFactory
    interface Factory : RecommendComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): RecommendComponentImpl
    }
}
