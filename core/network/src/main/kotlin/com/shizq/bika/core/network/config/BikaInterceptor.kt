package com.shizq.bika.core.network.config

import com.shizq.bika.core.network.util.API_KEY
import com.shizq.bika.core.network.util.DIGEST_KEY
import com.shizq.bika.core.network.util.signature
import io.ktor.http.HttpHeaders
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID

internal class BikaInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val time = (System.currentTimeMillis() / 1000).toString()
        val nonce = UUID.randomUUID().toString().replace("-", "")
        val request = originalRequest
            .newBuilder()
            .removeHeader(HttpHeaders.AcceptCharset)
            .removeHeader(HttpHeaders.Accept)
            .addHeader(HttpHeaders.Accept, "application/vnd.picacomic.com.v1+json")
            .addHeader("app-version", "2.2.1.2.3.4")
            .addHeader("app-uuid", "defaultUuid")
            .addHeader("app-platform", "android")
            .addHeader("app-build-version", "45")
            .addHeader("image-quality", "original")
            .addHeader("api-key", API_KEY)
            .addHeader("app-channel", "2")
            .addHeader("time", time)
            .addHeader("nonce", nonce)
            .addHeader(
                "signature",
                signature(
                    DIGEST_KEY,
                    (
                        originalRequest.url.pathSegments.joinToString("/") +
                            time +
                            nonce +
                            originalRequest.method +
                            API_KEY
                        )
                        .lowercase()
                )
            )
            .build()
        return chain.proceed(request)
    }
}
