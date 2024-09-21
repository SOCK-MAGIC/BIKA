package com.shizq.bika.core.network

import com.shizq.bika.core.network.model.NetworkToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class BikaNetworkDataSource @Inject constructor(private val client: HttpClient) {
    suspend fun signIn(username: String, password: String): NetworkToken =
        client.post("auth/sign-in") {
            setBody("""{"email":"$username","password":"$password"}""")
        }.body()
}


