package com.shizq.bika.core.network.di

import android.content.Context
import androidx.tracing.trace
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.serviceLoaderEnabled
import coil3.util.DebugLogger
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.network.BikaDispatchers
import com.shizq.bika.core.network.BuildConfig
import com.shizq.bika.core.network.Dispatcher
import com.shizq.bika.core.network.config.BikaClientPlugin
import com.shizq.bika.core.network.config.BikaInterceptor
import com.shizq.bika.core.network.util.PICA_API
import com.shizq.bika.core.network.util.asExecutorService
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Cache
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
        @Dispatcher(BikaDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        bikaInterceptor: BikaInterceptor,
    ): OkHttpClient = trace("BikaOkHttpClient") {
        OkHttpClient.Builder()
            .dispatcher(okhttp3.Dispatcher(ioDispatcher.limitedParallelism(64).asExecutorService()))
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
                url(PICA_API)
                userAgent("okhttp/3.8.1")
                contentType(ContentType.parse("application/json; charset=UTF-8"))
            }
            addDefaultResponseValidation()
            install(BikaClientPlugin) {
                transform = json
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
        client: Lazy<OkHttpClient>,
        @ApplicationContext application: Context,
    ): ImageLoader = trace("BikaImageLoader") {
        ImageLoader.Builder(application)
            .serviceLoaderEnabled(false)
            .components {
                add(
                    OkHttpNetworkFetcherFactory {
                        client.get().newBuilder()
                            .apply { interceptors().clear() }
                            .build()
                    }
                )
            }
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
