package com.shizq.bika.core.data.repository

import com.shizq.bika.core.data.Syncable
import com.shizq.bika.core.data.Synchronizer
import com.shizq.bika.core.data.dailyWork
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import javax.inject.Inject

class DailyTaskRepository @Inject constructor(
    private val network: BikaNetworkDataSource,
    private val niaPreferencesDataSource: BikaPreferencesDataSource,
) : Syncable {
    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.dailyWork(
            networkInit = {
                val (addresses, _) = network.networkInit()
                niaPreferencesDataSource.setResolveAddress(addresses)
            },
            punchIn = { network.punchIn() }
        )
    }
}
