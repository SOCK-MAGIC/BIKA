package com.shizq.bika.feature.signin

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SignInComponent {
    val account: StateFlow<SignInViewState>
    val viewEvents: Flow<LoginViewEvent>
    fun signIn(account: String, password: String)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): SignInComponent
    }
}

