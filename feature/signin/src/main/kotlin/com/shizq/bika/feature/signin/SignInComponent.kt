package com.shizq.bika.feature.signin

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow

interface SignInComponent {
    val viewEvents: Flow<LoginViewEvent>
    fun signIn()
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): SignInComponent
    }

    fun updateEmail(text: String)
    fun updatePassword(text: String)
    var email: String
    var password: String
}
