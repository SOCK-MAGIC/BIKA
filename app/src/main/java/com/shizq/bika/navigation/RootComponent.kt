package com.shizq.bika.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.shizq.bika.feature.signin.SignInComponent

interface RootComponent : ComponentContext {
    val stack: Value<ChildStack<*, Child>>

    fun navigationToSignIn()

    fun onBack()
    sealed class Child {
        data class SignIn(val component: SignInComponent) : Child()
    }

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): RootComponent
    }
}
