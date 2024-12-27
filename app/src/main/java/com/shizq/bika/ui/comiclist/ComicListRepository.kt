package com.shizq.bika.ui.comiclist

import com.shizq.bika.bean.ComicListBean
import com.shizq.bika.bean.ComicListBean2
import com.shizq.bika.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.URLEncoder

class ComicListRepository {
    fun getComicListFlow(
        tag: String,
        sort: String,
        page: Int,
        value: String
    ): Flow<Result<ComicListBean>> = flow {
        //     when (tag) {
        //         "categories" -> {
        //          val   urlEnd =
        //                 if (value == "") {
        //                     "comics?page=$page&s=$sort"
        //                 } else {
        //                     "comics?page=$page&c=${
        //                         URLEncoder.encode(value, "UTF-8").replace("\\+".toRegex(), "%20")
        //                     }&s=$sort"
        //                 }
        //             val headers = BaseHeaders(urlEnd, "GET").getHeaderMapAndToken()
        //                 if (value == "") {
        //                     emit(RetrofitUtil.service.comicsListGet(page.toString(), sort, headers))
        //                 } else {
        //                   emit(  RetrofitUtil.service.comicsListGet(
        //                         page.toString(),
        //                         value,
        //                         sort,
        //                         headers
        //                     ))
        //                 }
        //         }
        //
        //         "latest" -> {
        //             val urlEnd = "comics?page=$page&s=$sort"
        //             val headers = BaseHeaders(urlEnd, "GET").getHeaderMapAndToken()
        //               emit(RetrofitUtil.service.comicsListGet(page.toString(), sort, headers))
        //         }
        //
        //         "search" -> {
        //             val urlEnd = "comics/advanced-search?page=$page"
        //                  emit(RetrofitUtil.service.comicsSearchPOST(
        //                     page,
        //                      "{\"keyword\":\"$value\",\"sort\":\"$sort\"}"
        //                          .toRequestBody("application/json; charset=UTF-8".toMediaTypeOrNull()),
        //                     BaseHeaders(urlEnd, "POST").getHeaderMapAndToken()
        //                 ))
        //         }
        //
        //         "tags" -> {
        //             val urlEnd = "comics?page=$page&t=" + URLEncoder.encode(value, "UTF-8")
        //                 .replace("\\+".toRegex(), "%20")
        //             val headers = BaseHeaders(urlEnd, "GET").getHeaderMapAndToken()
        //              emit(RetrofitUtil.service.tagsGet(page.toString(), value, headers))
        //         }
        //
        //         "author" -> {
        //             val urlEnd = "comics?page=$page&a=" + URLEncoder.encode(value, "UTF-8")
        //                 .replace("\\+".toRegex(), "%20")
        //             val headers = BaseHeaders(urlEnd, "GET").getHeaderMapAndToken()
        //                  emit(RetrofitUtil.service.authorGet(page.toString(), value, headers))
        //         }
        //
        //         "knight" -> {
        //             val urlEnd = "comics?page=$page&ca=" + URLEncoder.encode(value, "UTF-8")
        //                 .replace("\\+".toRegex(), "%20")
        //             val headers = BaseHeaders(urlEnd, "GET").getHeaderMapAndToken()
        //                  emit(RetrofitUtil.service.creatorGet(page.toString(), value, headers))
        //         }
        //
        //         "translate" -> {
        //             val urlEnd = "comics?page=$page&ct=" + URLEncoder.encode(value, "UTF-8")
        //                 .replace("\\+".toRegex(), "%20")
        //             val headers = BaseHeaders(urlEnd, "GET").getHeaderMapAndToken()
        //                  emit(RetrofitUtil.service.translateGet(page.toString(), value, headers))
        //         }
        //
        //         "favourite" -> {
        //             val urlEnd = "users/favourite?s=$sort&page=$page"
        //             val headers = BaseHeaders(urlEnd, "GET").getHeaderMapAndToken()
        //              emit(RetrofitUtil.service.myFavouriteGet(sort, page.toString(), headers))
        //         }
        //     }
        // }.asResult().map {result->
        //     when(result){
        //         is com.shizq.bika.core.result.Result.Error -> TODO()
        //         com.shizq.bika.core.result.Result.Loading -> TODO()
        //         is com.shizq.bika.core.result.Result.Success -> TODO()
        //     }
    }

    fun getRandomFlow(): Flow<Result<ComicListBean2>> = flow {
        emit(Result.Loading)
        //     val headers = BaseHeaders("comics/random", "GET").getHeaderMapAndToken()
        //     val response = RetrofitUtil.service.randomGet(headers)
        //     if (response.code == 200) {
        //         val data = response.data
        //         if (data != null) {
        //             emit(Result.Success(data))
        //         } else {
        //             emit(Result.Error(response.code, "", "请求结果为空"))
        //         }
        //     } else {
        //         emit(Result.Error(response.code, response.error, response.message))
        //     }
        // }.catch {
        //     emit(Result.Error(-1, "", "请求结果异常"))
    }.flowOn(Dispatchers.IO)

    private fun getURLByTag(
        tag: String,
        sort: String,
        page: Int,
        value: String
    ): String {
        val s = URLEncoder.encode(value, "UTF-8")
            .replace("\\+".toRegex(), "%20")
        return when (tag) {
            "categories" -> "comics?page=$page&s=$sort" + if (value.isNotEmpty()) "&c=$s" else ""
            "latest" -> "comics?page=$page&s=$sort"
            "search" -> "comics/advanced-search?page=$page"
            "tags" -> "comics?page=$page&t=$s"
            "author" -> "comics?page=$page&a=$s"
            "knight" -> "comics?page=$page&ca=$s"
            "translate" -> "comics?page=$page&ct=$s"
            "favourite" -> "users/favourite?s=$sort&page=$page"
            else -> throw UnsupportedOperationException("不支持的 TAG")
        }
    }
}
