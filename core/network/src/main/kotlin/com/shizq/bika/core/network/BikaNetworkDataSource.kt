package com.shizq.bika.core.network

import android.util.Log
import com.shizq.bika.core.network.model.ComicInSearch
import com.shizq.bika.core.network.model.NetworkCategories
import com.shizq.bika.core.network.model.NetworkComicEpPicture
import com.shizq.bika.core.network.model.NetworkComicInfo
import com.shizq.bika.core.network.model.NetworkComicList
import com.shizq.bika.core.network.model.NetworkInit
import com.shizq.bika.core.network.model.NetworkProfile
import com.shizq.bika.core.network.model.NetworkPunchIn
import com.shizq.bika.core.network.model.NetworkToken
import com.shizq.bika.core.network.model.Sort
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
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
    suspend fun getComicEpPictures(
        id: String,
        epOrder: Int = 1,
        page: Int = 1,
    ): NetworkComicEpPicture =
        client.get("comics/$id/order/$epOrder/pages") {
            parameter("page", page)
        }.body()

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun advancedSearch(
        query: String,
        page: Int,
        sort: Sort = Sort.SORT_DEFAULT,
        categories: List<String> = emptyList(),
    ): ComicInSearch = client.post("comics/advanced-search") {
        parameter("page", page)
        setBody(
            buildJsonObject {
                put("keyword", query)
                put("sort", sort.value)
                putJsonArray("categories") {
                    addAll(categories.map { JsonPrimitive(it) })
                }
            },
        )
    }.body()
}
