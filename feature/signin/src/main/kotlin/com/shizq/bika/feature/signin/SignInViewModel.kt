package com.shizq.bika.feature.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
//     private val navigationToInterest: () -> Unit,
    private val network: BikaNetworkDataSource,
    private val userCredentialDataSource: BikaUserCredentialDataSource,
) : ViewModel() {
    val credentialState = userCredentialDataSource.userData
        .map { CredentialState.Success(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            CredentialState.Loading,
        )

    fun updateEmail(text: String) {
        viewModelScope.launch {
            userCredentialDataSource.setEmail(text)
        }
    }

    fun updatePassword(text: String) {
        viewModelScope.launch {
            userCredentialDataSource.setPassword(text)
        }
    }

    fun signIn() {
        viewModelScope.launch {
            val credential = userCredentialDataSource.userData.first()
            runCatching { network.signIn(credential.email, credential.password) }
                .onSuccess {
                    userCredentialDataSource.setToken(it.token)
//                    navigationToInterest()
                }.onFailure {
                }
        }
    }
}
