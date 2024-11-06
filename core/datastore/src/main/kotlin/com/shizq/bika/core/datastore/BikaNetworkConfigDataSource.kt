package com.shizq.bika.core.datastore

import androidx.datastore.core.DataStore
import com.shizq.bika.core.datastore.model.NetworkConfig
import javax.inject.Inject

class BikaNetworkConfigDataSource @Inject constructor(
    private val dataStore: DataStore<NetworkConfig>,
) {
    val networkConfig = dataStore.data

    suspend fun setResolveAddress(address: Set<String>) {
        dataStore.updateData {
            it.copy(dns = it.dns + address)
        }
    }
}
