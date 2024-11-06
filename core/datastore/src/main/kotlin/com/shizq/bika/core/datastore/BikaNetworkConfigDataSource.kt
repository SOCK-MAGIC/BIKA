package com.shizq.bika.core.datastore

import androidx.datastore.core.DataStore
import com.shizq.bika.core.datastore.model.NetworkConfig
import javax.inject.Inject

class BikaNetworkConfigDataSource @Inject constructor(
    private val network: DataStore<NetworkConfig>,
) {
    val networkConfig = network.data

    suspend fun setResolveAddress(address: Set<String>) {
        network.updateData {
            it.copy(dns = it.dns + address)
        }
    }
}
