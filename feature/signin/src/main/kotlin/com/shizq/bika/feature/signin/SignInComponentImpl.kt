package com.shizq.bika.feature.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import com.shizq.bika.core.network.BikaDispatchers
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.Dispatcher
import com.shizq.bika.core.network.di.ApplicationScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SignInComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
    private val userCredentialDataSource: BikaUserCredentialDataSource,
    @Dispatcher(BikaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : SignInComponent, ComponentContext by componentContext {
    override var email by mutableStateOf(runBlocking { userCredentialDataSource.userData.first().email })
    override var password by mutableStateOf(runBlocking { userCredentialDataSource.userData.first().password })

    private val _viewEvents = Channel<LoginViewEvent>(Channel.BUFFERED)
    override val viewEvents = _viewEvents.receiveAsFlow()

    init {
        componentScope.launch(ioDispatcher) {
            launch {
                snapshotFlow { email }
                    .collectLatest {
                        userCredentialDataSource.setEmail(it)
                    }
            }
            launch {
                snapshotFlow { password }
                    .collectLatest { userCredentialDataSource.setPassword(it) }
            }
        }
    }

    override fun updateEmail(text: String) {
        email = text
    }

    override fun updatePassword(text: String) {
        password = text
    }

    override fun signIn() {
        componentScope.launch {
            runCatching { network.signIn(email, password) }
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
