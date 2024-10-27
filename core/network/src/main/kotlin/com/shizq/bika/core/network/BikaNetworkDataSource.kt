package com.shizq.bika.core.network

import com.shizq.bika.core.network.model.ComicInSearch
import com.shizq.bika.core.network.model.Comics
import com.shizq.bika.core.network.model.NetworkCategories
import com.shizq.bika.core.network.model.NetworkComicEp
import com.shizq.bika.core.network.model.NetworkComicEpPicture
import com.shizq.bika.core.network.model.NetworkComicInfo
import com.shizq.bika.core.network.model.NetworkComicList
import com.shizq.bika.core.network.model.NetworkComicRandom
import com.shizq.bika.core.network.model.NetworkComicRecommend
import com.shizq.bika.core.network.model.NetworkFirstRecommend
import com.shizq.bika.core.network.model.NetworkInit
import com.shizq.bika.core.network.model.NetworkKnight
import com.shizq.bika.core.network.model.NetworkProfile
import com.shizq.bika.core.network.model.NetworkPunchIn
import com.shizq.bika.core.network.model.NetworkRankingDetail
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import javax.inject.Inject

class BikaNetworkDataSource @Inject constructor(
    private val client: HttpClient,
    @Dispatcher(BikaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
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
        comics: Comics? = null,
    ): NetworkComicList = client.get("comics") {
        parameter("c", comics?.category)
        parameter("t", comics?.tag)
        parameter("ca", comics?.creatorId)
        parameter("ct", comics?.chineseTeam)
        parameter("s", sort.value)
        parameter("a", comics?.author)
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
    suspend fun getRecommend(): NetworkFirstRecommend = client.get("collections").body()

    suspend fun favouriteComics(page: Int, sort: Sort = Sort.SORT_DEFAULT): NetworkComicList =
        client.get("users/favourite") {
            parameter("s", sort.value)
            parameter("page", page)
        }.body()

    /**
     * 喜欢/取消喜欢漫画
     */
    suspend fun switchLike(comicId: String) {
        val text = client.post("comics/$comicId/like").bodyAsText()
        // "action": "like"
        Napier.d(tag = "switchLike") { text }
    }

    /**
     * 收藏/取消收藏漫画
     */
    suspend fun switchFavourite(comicId: String) {
        val text = client.post("comics/$comicId/favourite").bodyAsText()
        // "action": "favourite"
        Napier.d(tag = "switchFavourite") { text }
    }

    /**
     * 获取漫画的评论
     */
    suspend fun comicComments(comicId: String, page: Int) {
        val text = client.get("comics/$comicId/comments") {
            parameter("page", page)
        }.bodyAsText()
        Napier.d(tag = "comicComments") { text }
    }

    suspend fun getComicAllEp(comicId: String): List<NetworkComicEp.Eps.Doc> =
        withContext(ioDispatcher) {
            val comicEp = getComicEp(comicId, 1)
            val docs = comicEp.eps.docs
            val pages = comicEp.eps.pages
            val result = if (pages < 2) {
                emptyList()
            } else {
                (2..pages).map { async { getComicEp(comicId, it) } }
                    .awaitAll()
                    .flatMap { it.eps.docs }
            }
            Napier.d(tag = "getComicAllEp") { "page:${comicEp.eps.page} pages:${comicEp.eps.pages} eps:${docs.size}" }
            comicEp.eps.docs + result
        }

    /**
     * 获取漫画章节
     */
    suspend fun getComicEp(comicId: String, page: Int): NetworkComicEp {
        return client.get("comics/$comicId/eps") {
            parameter("page", page)
        }.body<NetworkComicEp>()
    }

    suspend fun getRecommend(comicId: String): NetworkComicRecommend {
        return client.get("comics/$comicId/recommendation").body()
    }
}
