package com.shizq.bika.core.network.di

import androidx.tracing.trace
import com.shizq.bika.core.network.BikaInterceptor
import com.shizq.bika.core.network.ktor.BikaClientPlugin
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
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.userAgent
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
            .addInterceptor(BikaInterceptor())
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        json: Json,
        okHttpClient: OkHttpClient,
    ): HttpClient = trace("BikaHttpClient") {
        HttpClient(OkHttp) {
            engine {
                preconfigured = okHttpClient
            }
            defaultRequest {
                url("https://picaapi.picacomic.com/")
                userAgent("okhttp/3.8.1")
                contentType(ContentType.parse("application/json; charset=UTF-8"))
            }
            addDefaultResponseValidation()
            install(ContentNegotiation) {
                json(json)
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                exponentialDelay()
            }
            install(BikaClientPlugin) {
                transform = json
            }
        }
    }
}
