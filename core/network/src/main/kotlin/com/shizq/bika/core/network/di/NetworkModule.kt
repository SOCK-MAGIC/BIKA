package com.shizq.bika.core.network.di

import android.content.Context
import androidx.tracing.trace
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.network.okhttp.asNetworkClient
import coil3.util.DebugLogger
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.network.BuildConfig
import com.shizq.bika.core.network.config.BikaClientPlugin
import com.shizq.bika.core.network.config.BikaInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.addDefaultResponseValidation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.plugin
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Call
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Path.Companion.toOkioPath
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {
    @Provides
    @Singleton
    fun okHttpCallFactory(
        preferencesDataSource: BikaPreferencesDataSource,
        @ApplicationContext application: Context,
        bikaInterceptor: BikaInterceptor,
    ): OkHttpClient = trace("BikaOkHttpClient") {
        OkHttpClient.Builder()
            .dns {
                val dns = runBlocking { preferencesDataSource.userData.first().dns }
                dns.flatMap { Dns.SYSTEM.lookup(it) }
            }
            .addInterceptor(bikaInterceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .cache(Cache(File(application.cacheDir, "okhttp-cache"), 1024 * 1024 * 50))
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        json: Json,
        okHttpClient: OkHttpClient,
        preferencesDataSource: BikaPreferencesDataSource,
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
            install(BikaClientPlugin) {
                transform = json
                account = runBlocking { preferencesDataSource.userData.first().account }
                save = preferencesDataSource::setToken
            }
            install(ContentNegotiation) {
                json(json)
            }
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                exponentialDelay()
            }
        }
    }

    @Provides
    @Singleton
    fun imageLoader(
        @ApplicationContext application: Context,
    ): ImageLoader = trace("BikaImageLoader") {
        ImageLoader.Builder(application)
            .diskCache {
                DiskCache.Builder()
                    .directory(File(application.cacheDir, "coil-cache").toOkioPath())
                    .build()
            }
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    }
}
