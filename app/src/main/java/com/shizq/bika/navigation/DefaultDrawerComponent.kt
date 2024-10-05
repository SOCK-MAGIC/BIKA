package com.shizq.bika.navigation

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultDrawerComponent @AssistedInject constructor(@Assisted componentContext: ComponentContext) :
    DrawerComponent,
    ComponentContext by componentContext {
    @AssistedFactory
    interface Factory : DrawerComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultDrawerComponent
    }
}
