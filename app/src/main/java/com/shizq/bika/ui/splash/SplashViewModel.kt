package com.shizq.bika.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.result.Result
import com.shizq.bika.core.result.asResult
import com.shizq.bika.ui.splash.SplashActivityUiState.Failed
import com.shizq.bika.ui.splash.SplashActivityUiState.Loading
import com.shizq.bika.ui.splash.SplashActivityUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(
    private val network: BikaNetworkDataSource,
    preferencesDataSource: BikaPreferencesDataSource
) : ViewModel() {
    val uiState: StateFlow<SplashActivityUiState> = flow {
        val account = preferencesDataSource.userData.first().account
        emit(network.signIn(account.email, account.password))
    }.asResult()
        .map { result ->
            when (result) {
                is Result.Error -> Failed
                Result.Loading -> Loading
                is Result.Success -> {
                    val token = result.data.token
                    preferencesDataSource.setToken(token)
                    Success
                }
            }
        }.stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )
}

sealed interface SplashActivityUiState {
    data object Loading : SplashActivityUiState
    data object Failed : SplashActivityUiState
    data object Success : SplashActivityUiState
}
