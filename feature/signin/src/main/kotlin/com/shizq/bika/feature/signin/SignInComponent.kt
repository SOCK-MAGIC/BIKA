package com.shizq.bika.feature.signin

import com.arkivanov.decompose.ComponentContext

interface SignInComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): SignInComponent
    }
}

