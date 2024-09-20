package com.shizq.bika.core.network.di

import androidx.tracing.trace
import com.shizq.bika.core.network.BuildConfig
import com.shizq.bika.core.network.ktor.unwrapResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.addDefaultResponseValidation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {
    @Provides
    @Singleton
    fun okHttpCallFactory(): OkHttpClient = trace("BikaOkHttpClient") {
        OkHttpClient.Builder()
            .dns { Dns.SYSTEM.lookup("172.67.194.19") + Dns.SYSTEM.lookup("104.21.20.188") }
            .addInterceptor {
                val result = it.request()
                    .newBuilder()
                    .removeHeader("Accept-Charset")
                    .build()
                it.proceed(result)
            }
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    },
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        json: Json,
        okHttpClient: OkHttpClient,
    ): HttpClient {
        val client = HttpClient(
            OkHttp.create { preconfigured = okHttpClient },
        ) {
            defaultRequest {
                url("https://picaapi.picacomic.com")
                header(
                    HttpHeaders.ContentType,
                    ContentType.parse("application/json; charset=UTF-8")
                )
            }
            addDefaultResponseValidation()
            Logging()
            install(ContentNegotiation) {
                json(json, ContentType.parse("application/vnd.picacomic.com.v1+json"))
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                exponentialDelay()
            }
            install(unwrapResponse(json))
        }
        return client
    }
}
