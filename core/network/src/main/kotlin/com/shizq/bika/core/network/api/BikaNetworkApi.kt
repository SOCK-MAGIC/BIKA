package com.shizq.bika.core.network.api

import com.shizq.bika.core.network.model.Token
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

class BikaNetworkApi @Inject constructor(private val client: HttpClient) {
    suspend fun login(username: String, password: String): Token =
        client.post("auth/sign-in") {
            setBody("""{"email":"$username","password":"$password"}""")
        }.body()
}

@Serializable
data class Token(
    @SerialName("token")
    val token: String = ""
)
