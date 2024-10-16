package com.shizq.bika.feature.ranking

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class RankingComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : RankingComponent,
    ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : RankingComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): RankingComponentImpl
    }
}
