package com.shizq.bika.core.network.di

import androidx.tracing.trace
import com.shizq.bika.core.network.BuildConfig
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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Dns
import okhttp3.ExperimentalOkHttpApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideNetworkJson(): Json = trace("AsJson") {
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            // 使用默认值覆盖 null
            coerceInputValues = true
            prettyPrintIndent = "  "
        }
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): OkHttpClient = trace("NiaOkHttpClient") {
        OkHttpClient.Builder()
            .dns { Dns.SYSTEM.lookup("172.67.194.19") + Dns.SYSTEM.lookup("104.21.20.188") }
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
            }
            addDefaultResponseValidation()

            install(ContentNegotiation) {
                json(json)
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                exponentialDelay()
            }
        }
        return client
    }
}
