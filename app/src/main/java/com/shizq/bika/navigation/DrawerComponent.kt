package com.shizq.bika.navigation

import kotlinx.coroutines.flow.StateFlow

interface DrawerComponent {
    val uiState: StateFlow<DrawerUiState>
}

