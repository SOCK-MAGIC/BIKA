package com.shizq.bika.feature.signin

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.datastore.model.Account
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SignInComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
    private val preferencesDataSource: BikaPreferencesDataSource
) : SignInComponent, ComponentContext by componentContext {
    val acc = preferencesDataSource.userData.map { preferences ->
        preferences.account?.let { JVMD.Success(it) } ?: JVMD.Failed
    }.stateIn(
        componentScope,
        SharingStarted.WhileSubscribed(5000),
        JVMD.Failed
    )
    fun signIn(email:String, password:String){
        // network.signIn(email,password)
    }
    @AssistedFactory
    interface Factory : SignInComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): SignInComponentImpl
    }
}

sealed interface JVMD {
    data class Success(val account: Account) : JVMD
    data object Failed : JVMD
}
