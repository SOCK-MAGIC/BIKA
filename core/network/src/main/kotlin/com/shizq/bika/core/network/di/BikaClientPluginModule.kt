package com.shizq.bika.core.network.di

import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import com.shizq.bika.core.network.util.API_KEY
import com.shizq.bika.core.network.util.DIGEST_KEY
import com.shizq.bika.core.network.util.PICA_API
import com.shizq.bika.core.network.util.signature
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BikaClientPluginModule {

    @Provides
    @Singleton
    fun bikaRequestSignature(
        bikaUserCredentialDataSource: BikaUserCredentialDataSource,
    ): ClientPlugin<RequestSignatureConfig> =
        createClientPlugin("BikaRequestSignature", ::RequestSignatureConfig) {
            onRequest { request, _ ->
                if (request.url.encodedPathSegments.contains("init")) return@onRequest
                val parameter = request.url.toString().replace(PICA_API, "")
                val time = (System.currentTimeMillis() / 1000).toString()
                val nonce = UUID.randomUUID().toString().replace("-", "")
                val text = (parameter + time + nonce + request.method.value + API_KEY).lowercase()
                with(request) {
                    header(
                        HttpHeaders.Authorization,
                        bikaUserCredentialDataSource.userData.first().token,
                    )
                    header("app-version", "2.2.1.2.3.4")
                    header("app-uuid", "defaultUuid")
                    header("app-platform", "android")
                    header("app-build-version", "45")
                    header("image-quality", "original")
                    header("api-key", API_KEY)
                    header("app-channel", "2")
                    header("time", time)
                    header("nonce", nonce)
                    header("signature", signature(DIGEST_KEY, text))
                }
            }
            on(Send) { request ->
                request.headers.remove(HttpHeaders.AcceptCharset)
                request.headers[HttpHeaders.Accept] = "application/vnd.picacomic.com.v1+json"
                proceed(request)
            }
        }
}
