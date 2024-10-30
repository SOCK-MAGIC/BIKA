package com.shizq.bika.navigation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface DrawerComponent {
    val uiState: StateFlow<DrawerUiState>

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): DrawerComponent
    }
}

