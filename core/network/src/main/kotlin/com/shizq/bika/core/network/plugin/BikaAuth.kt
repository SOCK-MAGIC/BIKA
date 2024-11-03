package com.shizq.bika.core.network.plugin

import com.shizq.bika.core.network.util.API_KEY
import com.shizq.bika.core.network.util.DIGEST_KEY
import com.shizq.bika.core.network.util.PICA_API
import com.shizq.bika.core.network.util.signature
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.KtorDsl
import java.util.UUID

/**
 * Install [BikaAuth] plugin.
 */
public fun HttpClientConfig<*>.BikaAuth(block: BikaAuthConfig.() -> Unit) {
    install(BikaAuth, block)
}

/**
 * todo 添加刷新 token
 */
@KtorDsl
public class BikaAuthConfig {

    internal var _credentials: suspend () -> BikaAuthCredentials? = { null }

    /**
     * Allows you to specify authentication credentials.
     */
    public fun credentials(block: suspend () -> BikaAuthCredentials?) {
        _credentials = block
    }
}

val BikaAuth = createClientPlugin("BikaRequestSignature", ::BikaAuthConfig) {
    val credentials = pluginConfig._credentials
    onRequest { request, _ ->
        if (request.url.encodedPathSegments.contains("init")) return@onRequest
        val parameter = request.url.toString().replace(PICA_API, "")
        val time = (System.currentTimeMillis() / 1000).toString()
        val nonce = UUID.randomUUID().toString().replace("-", "")
        val text = (parameter + time + nonce + request.method.value + API_KEY).lowercase()
        with(request) {
            header(HttpHeaders.Authorization, credentials.invoke()?.token)
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

class BikaAuthCredentials(val token: String)
