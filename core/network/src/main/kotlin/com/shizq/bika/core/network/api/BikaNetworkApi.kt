package com.shizq.bika.core.network.api

import com.shizq.bika.core.network.model.Token
import com.shizq.bika.core.network.util.header
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import javax.inject.Inject

class BikaNetworkApi @Inject constructor(private val client: HttpClient) {
    suspend fun login(username: String, password: String, headers: Map<String, String>): Token =
        client.post("auth/sign-in") {
            header(headers)
            setBody(
                buildJsonObject {
                    put("email", JsonPrimitive("socksmagic"))
                    put("password", JsonPrimitive("liqingquan"))
                }.toString()
            )
        }.body()
}
