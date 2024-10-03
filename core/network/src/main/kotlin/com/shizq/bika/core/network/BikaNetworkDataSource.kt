package com.shizq.bika.core.network

import com.shizq.bika.core.network.model.NetworkCategories
import com.shizq.bika.core.network.model.NetworkComicInfo
import com.shizq.bika.core.network.model.NetworkComicList
import com.shizq.bika.core.network.model.NetworkInit
import com.shizq.bika.core.network.model.NetworkProfile
import com.shizq.bika.core.network.model.NetworkPunchIn
import com.shizq.bika.core.network.model.NetworkToken
import com.shizq.bika.core.network.model.Sort
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.encodeURLParameter
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
    suspend fun getComicList(
        sort: Sort,
        page: Int,
        category: String? = null,
        tag: String? = null,
        creatorId: String? = null,
        chineseTeam: String? = null,
    ): NetworkComicList = client.get("comics") {
        parameter("c", category)
        parameter("t", tag)
        parameter("ca", creatorId)
        parameter("ct", chineseTeam)
        parameter("s", sort.value)
        parameter("page", page)
    }.body()

    suspend fun getComicInfo(id: String): NetworkComicInfo = client.get("comics/$id").body()
}
