package com.shizq.bika.feature.signin

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SignInComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val navigationToInterest: () -> Unit,
    private val network: BikaNetworkDataSource,
    private val userCredentialDataSource: BikaUserCredentialDataSource,
) : SignInComponent,
    ComponentContext by componentContext {
    override val credentialState = userCredentialDataSource.userData
        .map { CredentialState.Success(it) }
        .stateIn(
            componentScope,
            SharingStarted.WhileSubscribed(5000),
            CredentialState.Loading,
        )

    override fun updateEmail(text: String) {
        componentScope.launch {
            userCredentialDataSource.setEmail(text)
        }
    }

    override fun updatePassword(text: String) {
        componentScope.launch {
            userCredentialDataSource.setPassword(text)
        }
    }

    override fun signIn() {
        componentScope.launch {
            val credential = userCredentialDataSource.userData.first()
            runCatching { network.signIn(credential.email, credential.password) }
                .onSuccess {
                    userCredentialDataSource.setToken(it.token)
                    navigationToInterest()
                }.onFailure {
                }
        }
    }

    @AssistedFactory
    interface Factory : SignInComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            navigationToInterest: () -> Unit,
        ): SignInComponentImpl
    }
}
