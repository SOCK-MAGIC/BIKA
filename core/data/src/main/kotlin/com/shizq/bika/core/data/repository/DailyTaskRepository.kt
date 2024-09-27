package com.shizq.bika.core.data.repository

import com.shizq.bika.core.data.Syncable
import com.shizq.bika.core.data.Synchronizer
import com.shizq.bika.core.data.dailyWork
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DailyTaskRepository @Inject constructor(
    private val network: BikaNetworkDataSource,
    private val bikaPreferencesDataSource: BikaPreferencesDataSource,
) : Syncable {
    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.dailyWork(
            networkInit = {
                val (addresses, _) = network.networkInit()
                bikaPreferencesDataSource.setResolveAddress(addresses)
            },
            punchIn = { network.punchIn() },
            signIn = {
                val account = bikaPreferencesDataSource.userData.first().account
                val token = account?.let { network.signIn(it.email, account.password) }
                if (token != null) {
                    bikaPreferencesDataSource.setToken(token.token)
                }
            }
        )
    }
}
