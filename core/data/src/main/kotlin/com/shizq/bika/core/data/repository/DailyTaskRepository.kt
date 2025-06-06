package com.shizq.bika.core.data.repository

import android.util.Log
import com.shizq.bika.core.data.Syncable
import com.shizq.bika.core.data.Synchronizer
import com.shizq.bika.core.data.dailyWork
import com.shizq.bika.core.datastore.BikaNetworkConfigDataSource
import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DailyTaskRepository @Inject constructor(
    private val network: BikaNetworkDataSource,
    private val bikaUserCredentialDataSource: BikaUserCredentialDataSource,
    private val bikaNetworkConfigDataSource: BikaNetworkConfigDataSource,
) : Syncable {
    override suspend fun syncWith(synchronizer: Synchronizer): Boolean = synchronizer.dailyWork(
        networkInit = {
            val (addresses, _) = network.networkInit()
            bikaNetworkConfigDataSource.setResolveAddress(addresses)
            Log.i("DailyTaskRepository", addresses.toString())
        },
        punchIn = {
            val response = network.punchIn()
            Log.i("DailyTaskRepository", response.toString())
        },
        signIn = {
            val userCredential = bikaUserCredentialDataSource.userData.first()
            val email = userCredential.email
            val password = userCredential.password
            if (userCredential.token.isNotEmpty()) {
                val token = network.signIn(email, password)
                bikaUserCredentialDataSource.setToken(token.token)
                Log.i("DailyTaskRepository", token.toString())
            }
        },
    )
}
