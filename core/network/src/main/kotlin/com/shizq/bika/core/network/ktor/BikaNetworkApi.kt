package com.shizq.bika.core.network.ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import javax.inject.Inject

class BikaNetworkApi @Inject constructor(private val client: HttpClient) {

    suspend fun login(username: String, password: String, headers: Map<String, String>) {
        client.post("auth/sign-in") {
            setBody(
                buildJsonObject {
                    put("email", JsonPrimitive(username))
                    put("password", JsonPrimitive(password))
                }
            )
            headers.forEach { (k, v) ->
                header(k, v)
            }
        }
    }
}
