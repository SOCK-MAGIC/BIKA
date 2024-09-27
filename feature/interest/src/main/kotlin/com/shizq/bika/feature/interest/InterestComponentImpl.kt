package com.shizq.bika.feature.interest

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class InterestComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext
) : InterestComponent {
    @AssistedFactory
    interface Factory : InterestComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): InterestComponentImpl
    }
}
