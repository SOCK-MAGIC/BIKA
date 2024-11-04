package com.shizq.bika.core.network.di

import android.content.Context
import androidx.tracing.trace
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.size.Precision
import coil3.util.DebugLogger
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import com.shizq.bika.core.network.BikaDispatchers
import com.shizq.bika.core.network.BuildConfig
import com.shizq.bika.core.network.Dispatcher
import com.shizq.bika.core.network.coil.MergeRequestInterceptor
import com.shizq.bika.core.network.data.HttpResponseMonitor
import com.shizq.bika.core.network.model.Box
import com.shizq.bika.core.network.plugin.auth.BikaAuth
import com.shizq.bika.core.network.plugin.auth.BikaAuthCredentials
import com.shizq.bika.core.network.plugin.contentunboxing.ContentUnboxing
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
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import okhttp3.Cache
import okhttp3.Dns
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
internal val NetworkJson: Json =
    Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        coerceInputValues = true
        prettyPrintIndent = "  "
        encodeDefaults = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
        useArrayPolymorphism = false
    }

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {
    @Provides
    @Singleton
    fun okHttpClient(
        preferencesDataSource: BikaPreferencesDataSource,
        @ApplicationContext application: Context,
        @Dispatcher(BikaDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): OkHttpClient = trace("BikaOkHttpClient") {
        OkHttpClient.Builder()
            .dispatcher(okhttp3.Dispatcher(ioDispatcher.asExecutorService()))
            .dns {
                val dns = runBlocking { preferencesDataSource.userData.first().dns }
                dns.flatMap { Dns.SYSTEM.lookup(it) }
            }
            .cache(Cache(application.cacheDir.resolve("okhttp-cache"), 1024 * 1024 * 500))
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        okHttpClient: OkHttpClient,
        bikaUserCredentialDataSource: BikaUserCredentialDataSource,
        httpResponseMonitor: HttpResponseMonitor,
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
            install(ContentUnboxing) {
                transform { response, content, requestedType ->
                    val box = NetworkJson.decodeFromStream(
                        Box.serializer(serializer(requestedType.kotlinType!!)),
                        content.toInputStream(),
                    )
                    box.data
                }
            }
            install(ContentNegotiation) {
                json(NetworkJson)
            }
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                exponentialDelay()
            }
            Logging {
                level = LogLevel.ALL
                logger = Logger.ANDROID
            }
            BikaAuth {
                credentials {
                    BikaAuthCredentials(bikaUserCredentialDataSource.userData.first().token)
                }
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
            .components {
                add(MergeRequestInterceptor())
                add(
                    OkHttpNetworkFetcherFactory(
                        client.get().newBuilder()
                            .apply { interceptors().clear() }
                            .build(),
                    ),
                )
            }
            .precision(Precision.INEXACT)
            .diskCache {
                DiskCache.Builder()
                    .directory(application.cacheDir.resolve("coil-cache"))
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
