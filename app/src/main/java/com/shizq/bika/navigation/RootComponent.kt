package com.shizq.bika.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface RootComponent : ComponentContext {
    val stack: Value<ChildStack<*, Child>>

    fun navigationToSignIn()

    fun onBack()
    sealed class Child

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): RootComponent
    }
}
