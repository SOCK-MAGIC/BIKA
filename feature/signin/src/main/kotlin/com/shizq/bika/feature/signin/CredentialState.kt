package com.shizq.bika.feature.signin

import com.shizq.bika.core.datastore.model.UserCredential

sealed interface CredentialState {
    data object Loading : CredentialState
    data class Success(val userCredential: UserCredential) : CredentialState
}