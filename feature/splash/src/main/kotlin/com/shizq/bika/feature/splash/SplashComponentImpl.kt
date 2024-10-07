package com.shizq.bika.feature.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SplashComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val userCredential: BikaUserCredentialDataSource
) : SplashComponent, ComponentContext by componentContext {
    override val hasToken by mutableStateOf(runBlocking { userCredential.userData.first().token.isNotEmpty() })

    @AssistedFactory
    interface Factory : SplashComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): SplashComponentImpl
    }
}
