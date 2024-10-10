package com.shizq.bika.feature.signup

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SignUpComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
    private val preferencesDataSource: BikaPreferencesDataSource,
) : SignUpComponent,
    ComponentContext by componentContext {
    @AssistedFactory
    interface Factory : SignUpComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): SignUpComponentImpl
    }
}
