package com.shizq.bika.feature.signin

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SignInComponent {
    val viewEvents: Flow<LoginViewEvent>
    fun signIn(account: String, password: String)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): SignInComponent
    }

    val userCredential: StateFlow<UserCredential>
    fun updateEmail(email: String)
    fun updatePassword(password: String)
}

