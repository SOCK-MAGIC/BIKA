package com.shizq.bika.feature.signin

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SignInComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : SignInComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : SignInComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): SignInComponentImpl
    }
}
