package com.shizq.bika.feature.signin

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
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
    private val preferencesDataSource: BikaPreferencesDataSource
) : SignInComponent, ComponentContext by componentContext {
    private val _viewEvents = Channel<LoginViewEvent>(Channel.BUFFERED)
    override val viewEvents = _viewEvents.receiveAsFlow()

    override val account = preferencesDataSource.userData.map {
        SignInViewState(it.account?.email ?: "", it.account?.password ?: "")
    }.stateIn(
        componentScope,
        SharingStarted.WhileSubscribed(5000),
        SignInViewState()
    )

    override fun signIn(account: String, password: String) {
        componentScope.launch {
            runCatching { network.signIn(account, password) }
                .onSuccess {
                    preferencesDataSource.setToken(it.token)
                    preferencesDataSource.setAccount(account, password)
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
