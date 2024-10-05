package com.shizq.bika.feature.signin

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SignInComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
    private val userCredentialDataSource: BikaUserCredentialDataSource,
) : SignInComponent, ComponentContext by componentContext {
    private val _viewEvents = Channel<LoginViewEvent>(Channel.BUFFERED)
    override val viewEvents = _viewEvents.receiveAsFlow()

    override val userCredential = userCredentialDataSource.userData.map {
        UserCredential(it.email, it.password)
    }.stateIn(
        componentScope,
        SharingStarted.WhileSubscribed(5_000),
        UserCredential("", "")
    )

    override fun updateEmail(email: String) {
        componentScope.launch {
            userCredentialDataSource.setEmail(email)
        }
    }

    override fun updatePassword(password: String) {
        componentScope.launch {
            userCredentialDataSource.setPassword(password)
        }
    }

    override fun signIn(account: String, password: String) {
        componentScope.launch {
            runCatching { network.signIn(account, password) }
                .onSuccess {
                    userCredentialDataSource.setToken(it.token)
                    _viewEvents.send(LoginViewEvent.NavTo)
                }.onFailure {
                    _viewEvents.send(LoginViewEvent.ShowMessage(it.message ?: ""))
                }
        }
    }

    @AssistedFactory
    interface Factory : SignInComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): SignInComponentImpl
    }
}
