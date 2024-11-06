package com.shizq.bika.navigation

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.NetworkUser
import com.shizq.bika.core.result.Result
import com.shizq.bika.core.result.asResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DefaultDrawerComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
) : DrawerComponent,
    ComponentContext by componentContext {
    override val uiState = flow { emit(network.getUserProfile()) }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> DrawerUiState.Loading
                Result.Loading -> DrawerUiState.Loading
                is Result.Success -> DrawerUiState.Success(result.data.netWorkUser)
            }
        }
        .stateIn(
            componentScope,
            SharingStarted.WhileSubscribed(5000),
            DrawerUiState.Loading,
        )

    @AssistedFactory
    interface Factory : DrawerComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultDrawerComponent
    }
}

sealed interface DrawerUiState {
    data object Loading : DrawerUiState
    data class Success(val netWorkUser: NetworkUser) : DrawerUiState
}
