package com.shizq.bika.core.network

import com.shizq.bika.core.network.model.ComicInSearch
import com.shizq.bika.core.network.model.NetworkCategories
import com.shizq.bika.core.network.model.NetworkComicEpPicture
import com.shizq.bika.core.network.model.NetworkComicInfo
import com.shizq.bika.core.network.model.NetworkComicList
import com.shizq.bika.core.network.model.NetworkComicRandom
import com.shizq.bika.core.network.model.NetworkInit
import com.shizq.bika.core.network.model.NetworkKnight
import com.shizq.bika.core.network.model.NetworkProfile
import com.shizq.bika.core.network.model.NetworkPunchIn
import com.shizq.bika.core.network.model.NetworkRankingDetail
import com.shizq.bika.core.network.model.NetworkRecommend
import com.shizq.bika.core.network.model.NetworkToken
import com.shizq.bika.core.network.model.Sort
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import javax.inject.Inject

class BikaNetworkDataSource @Inject constructor(private val client: HttpClient) {
    suspend fun networkInit(): NetworkInit = client.get("http://68.183.234.72/init").body()

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

    suspend fun getRankingDetail(titleType: String): NetworkRankingDetail =
        client.get("comics/leaderboard") {
            parameter("tt", titleType)
            parameter("ct", "VC")
        }.body()

    suspend fun getKnightRankingDetail(): NetworkKnight =
        client.get("comics/knight-leaderboard").body()

    /**
     * 随机漫画
     */
    suspend fun comicsRandom(): NetworkComicRandom = client.get("comics/random").body()
    suspend fun getRecommend(): NetworkRecommend = client.get("collections").body()

    suspend fun favouriteComics(page: Int, sort: Sort = Sort.SORT_DEFAULT): NetworkComicList =
        client.get("users/favourite") {
            parameter("s", sort.value)
            parameter("page", page)
        }.body()
}
