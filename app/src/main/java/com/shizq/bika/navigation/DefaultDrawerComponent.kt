package com.shizq.bika.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.NetworkUser
import com.shizq.bika.core.result.Result
import com.shizq.bika.core.result.asResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class DefaultDrawerComponent @Inject constructor(
    private val network: BikaNetworkDataSource,
) : DrawerComponent, ViewModel() {
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
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DrawerUiState.Loading,
        )
}

sealed interface DrawerUiState {
    data object Loading : DrawerUiState
    data class Success(val netWorkUser: NetworkUser) : DrawerUiState
}
