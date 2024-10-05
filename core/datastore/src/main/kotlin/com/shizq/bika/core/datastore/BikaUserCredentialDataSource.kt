package com.shizq.bika.core.datastore

import androidx.datastore.core.DataStore
import com.shizq.bika.core.datastore.model.UserCredential
import javax.inject.Inject

class BikaUserCredentialDataSource @Inject constructor(
    private val userCredential: DataStore<UserCredential>,
) {
    val userData = userCredential.data

    suspend fun setToken(token: String) {
        userCredential.updateData {
            it.copy(token = token)
        }
    }

    suspend fun setEmail(email: String) {
        userCredential.updateData {
            it.copy(email = email)
        }
    }

    suspend fun setPassword(password: String) {
        userCredential.updateData {
            it.copy(password = password)
        }
    }
}
