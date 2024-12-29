package com.shizq.bika.feature.signin

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface SignInComponent {
    fun signIn()
    fun updateEmail(text: String)
    fun updatePassword(text: String)
    val credentialState: StateFlow<CredentialState>

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            navigationToInterest: () -> Unit,
        ): SignInComponent
    }
}
