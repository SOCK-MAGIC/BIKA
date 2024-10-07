package com.shizq.bika.core.data.repository

import com.shizq.bika.core.data.Syncable
import com.shizq.bika.core.data.Synchronizer
import com.shizq.bika.core.data.dailyWork
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DailyTaskRepository @Inject constructor(
    private val network: BikaNetworkDataSource,
    private val bikaPreferencesDataSource: BikaPreferencesDataSource,
    private val bikaUserCredentialDataSource: BikaUserCredentialDataSource,
) : Syncable {
    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.dailyWork(
            networkInit = {
                val (addresses, _) = network.networkInit()
                bikaPreferencesDataSource.setResolveAddress(addresses)
            },
            punchIn = { network.punchIn() },
            signIn = {
                val userCredential = bikaUserCredentialDataSource.userData.first()
                val email = userCredential.email
                val password = userCredential.password
                if (userCredential.token.isNotEmpty()) {
                    val token = network.signIn(email, password)
                    bikaUserCredentialDataSource.setToken(token.token)
                }
            }
        )
    }
}
