package com.shizq.bika.core.network.plugin

import com.shizq.bika.core.network.util.API_KEY
import com.shizq.bika.core.network.util.DIGEST_KEY
import com.shizq.bika.core.network.util.PICA_API
import com.shizq.bika.core.network.util.signature
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import java.util.UUID

val BikaSignature = createClientPlugin("BikaRequestSignature", ::RequestSignatureConfig) {
    val token = pluginConfig.token
    onRequest { request, _ ->
        if (request.url.encodedPathSegments.contains("init")) return@onRequest
        val parameter = request.url.toString().replace(PICA_API, "")
        val time = (System.currentTimeMillis() / 1000).toString()
        val nonce = UUID.randomUUID().toString().replace("-", "")
        val text = (parameter + time + nonce + request.method.value + API_KEY).lowercase()
        with(request) {
            header(HttpHeaders.Authorization, token)
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

class RequestSignatureConfig {
    private var _token: String? = null
    public var token: String
        get() =_token?: error("")
        set(value) {
            _token = value
        }
}
