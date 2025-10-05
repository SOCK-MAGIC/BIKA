package com.shizq.bika.feature.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userCredential: BikaUserCredentialDataSource,
) : ViewModel() {
    val hasToken by mutableStateOf(runBlocking { userCredential.userData.first().token.isNotEmpty() })
}
