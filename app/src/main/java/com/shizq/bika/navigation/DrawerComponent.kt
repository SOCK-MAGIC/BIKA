package com.shizq.bika.navigation

import com.arkivanov.decompose.ComponentContext

interface DrawerComponent {

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): DrawerComponent
    }
}

