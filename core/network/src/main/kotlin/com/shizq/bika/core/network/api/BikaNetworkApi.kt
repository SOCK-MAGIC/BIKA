package com.shizq.bika.core.network.api

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import javax.inject.Inject

class BikaNetworkApi @Inject constructor(private val client: HttpClient) {
    //{
    //2024-09-15 17:26:50.417  6613-7172  okhttp.OkHttpClient     com.shizq.bika                       I    "code": 200,
    //2024-09-15 17:26:50.417  6613-7172  okhttp.OkHttpClient     com.shizq.bika                       I    "message": "success",
    //2024-09-15 17:26:50.417  6613-7172  okhttp.OkHttpClient     com.shizq.bika                       I    "data": {
    //2024-09-15 17:26:50.417  6613-7172  okhttp.OkHttpClient     com.shizq.bika                       I      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OGM3YjRhMWQyODBiNjRiZTdlOWE3YTQiLCJlbWFpbCI6InNvY2tzbWFnaWMiLCJyb2xlIjoibWVtYmVyIiwibmFtZSI6IuiinOWtkOmtlCIsInZlcnNpb24iOiIyLjIuMS4zLjMuNCIsImJ1aWxkVmVyc2lvbiI6IjQ1IiwicGxhdGZvcm0iOiJhbmRyb2lkIiwiaWF0IjoxNzI2MzkyNDExLCJleHAiOjE3MjY5OTcyMTF9.TfVxF15jUoDLfSbyOFLqIeI8w4IEwZhcXwVhngCLWRY"
    //2024-09-15 17:26:50.417  6613-7172  okhttp.OkHttpClient     com.shizq.bika                       I    }
    //2024-09-15 17:26:50.418  6613-7172  okhttp.OkHttpClient     com.shizq.bika                       I  }
    suspend fun login(username: String, password: String, headers: Map<String, String>) {
        client.post("auth/sign-in") {
            headers.forEach { (k, v) ->
                header(k, v)
            }
            setBody(
                buildJsonObject {
                    put("email", JsonPrimitive(username))
                    put("password", JsonPrimitive(password))
                }.toString()
            )
        }
    }
}
