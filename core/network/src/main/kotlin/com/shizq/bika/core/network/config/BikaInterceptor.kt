package com.shizq.bika.core.network.config

import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import com.shizq.bika.core.network.util.API_KEY
import com.shizq.bika.core.network.util.DIGEST_KEY
import com.shizq.bika.core.network.util.PICA_API
import com.shizq.bika.core.network.util.signature
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID
import javax.inject.Inject

internal class BikaInterceptor @Inject constructor(
    private val bikaUserCredentialDataSource: BikaUserCredentialDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { bikaUserCredentialDataSource.userData.first().token }
        val originalRequest = chain.request()
        val parameter = originalRequest.url.toString().replace(PICA_API, "")
        val time = (System.currentTimeMillis() / 1000).toString()
        val nonce = UUID.randomUUID().toString().replace("-", "")
        val text = (parameter + time + nonce + originalRequest.method + API_KEY).lowercase()
        val request = originalRequest
            .newBuilder()
            .removeHeader(HttpHeaders.AcceptCharset)
            .addHeader(HttpHeaders.Authorization, token)
            .header(HttpHeaders.Accept, "application/vnd.picacomic.com.v1+json")
            .addHeader("app-version", "2.2.1.2.3.4")
            .addHeader("app-uuid", "defaultUuid")
            .addHeader("app-platform", "android")
            .addHeader("app-build-version", "45")
            .addHeader("image-quality", "original")
            .addHeader("api-key", API_KEY)
            .addHeader("app-channel", "2")
            .addHeader("time", time)
            .addHeader("nonce", nonce)
            .addHeader("signature", signature(DIGEST_KEY, text))
            .build()
        return chain.proceed(request)
    }
}
