package com.shizq.bika.feature.signup

import com.arkivanov.decompose.ComponentContext

interface SignUpComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): SignUpComponent
    }
}
