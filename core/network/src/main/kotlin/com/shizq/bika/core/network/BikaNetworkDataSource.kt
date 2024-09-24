package com.shizq.bika.core.network

import com.shizq.bika.core.network.model.NetworkCategories
import com.shizq.bika.core.network.model.NetworkInit
import com.shizq.bika.core.network.model.NetworkProfile
import com.shizq.bika.core.network.model.NetworkPunchIn
import com.shizq.bika.core.network.model.NetworkToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class BikaNetworkDataSource @Inject constructor(private val client: HttpClient) {
    suspend fun networkInit(): NetworkInit = client.get("init").body()

    suspend fun signIn(username: String, password: String): NetworkToken =
        client.post("auth/sign-in") {
            setBody("""{"email":"$username","password":"$password"}""")
        }.body()

    suspend fun punchIn(): NetworkPunchIn = client.post("users/punch-in").body()
    suspend fun profile(): NetworkProfile = client.post("users/profile").body()
    suspend fun getCategories(): NetworkCategories = client.get("categories").body()
}