package com.shizq.bika.core.datastore

import androidx.datastore.core.DataStore
import com.shizq.bika.core.datastore.model.Account
import com.shizq.bika.core.datastore.model.UserPreferences
import javax.inject.Inject

class BikaPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data

    @Deprecated("BikaUserCredentialDataSource")
    suspend fun setToken(token: String) {
        userPreferences.updateData {
            it.copy(token = token)
        }
    }

    suspend fun setResolveAddress(addresses: Set<String>) {
        userPreferences.updateData {
            it.copy(dns = it.dns + addresses)
        }
    }
}
