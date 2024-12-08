package com.shizq.bika.network

import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitUtil {
    private var retrofit: Retrofit? = null

    private const val BASE_URL = "https://picaapi.picacomic.com"
    private const val UPDATE = "https://appcenter.ms"
    var LIVE_SERVER = "https://live-server.bidobido.xyz" // 新聊天室

    val service: ApiService by lazy {
        getRetrofit(BASE_URL).create(ApiService::class.java)
    }

    val service_update: ApiService by lazy {
        getRetrofit(UPDATE).create(ApiService::class.java)
    }

    val service_live: ApiService by lazy {
        getRetrofit(LIVE_SERVER).create(ApiService::class.java)
    }

    private fun getRetrofit(url: String): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        return retrofit!!
    }
}
