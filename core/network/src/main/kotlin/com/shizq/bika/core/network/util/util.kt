package com.shizq.bika.core.network.util

import io.ktor.http.HttpMessageBuilder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

internal const val API_KEY = "C69BAF41DA5ABD1FFEDC6D2FEA56B"
internal const val DIGEST_KEY = "~d}\$Q7\$eIni=V)9\\RK/P.RM4;9[7|@/CA}b~OW!3?EV`:<>M7pddUBL5n|0/*Cn"

internal fun HttpMessageBuilder.header(map: Map<String, String>) {
    map.forEach { (k,v)-> headers.append(k, v) }
}

@OptIn(ExperimentalStdlibApi::class)
internal fun signature(key: String, text: String): String {
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(SecretKeySpec(key.toByteArray(), "HmacSHA256"))
    return mac.doFinal(text.toByteArray()).toHexString()
}
