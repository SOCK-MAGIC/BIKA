package com.shizq.bika.core.network.util

import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class BaseHeaders(private val urlEnd: String = "", val type: String = "") {

    fun getHeaders(): MutableMap<String, String> {
        val apikey = "C69BAF41DA5ABD1FFEDC6D2FEA56B"
        val nonce = generateUUIDString(UUID.randomUUID())
        val time = (System.currentTimeMillis() / 1000).toString()
//        val signature = HmacSHA256Util().C((urlEnd + time + nonce + type + apikey))
        val map: MutableMap<String, String> = HashMap()
        map["api-key"] = apikey
//        map["accept"] = "application/vnd.picacomic.com.v1+json"
        map["app-channel"] = 3.toString()
        map["time"] = time
        map["nonce"] = nonce
//        map["signature"] = signature
        map["app-version"] = "2.2.1.3.3.4"
        map["app-uuid"] = "defaultUuid"
        map["image-quality"] = "original"
        map["app-platform"] = "android"
        map["app-build-version"] = "45"
        map["user-agent"] = "okhttp/3.8.1"
        return map
    }
}

private fun generateUUIDString(uuid: UUID): String {
    return (digits(uuid.mostSignificantBits shr 32, 8) +
            digits(uuid.mostSignificantBits shr 16, 4) +
            digits(uuid.mostSignificantBits, 4) +
            digits(uuid.leastSignificantBits shr 48, 4) +
            digits(uuid.leastSignificantBits, 12))
}

private fun digits(`val`: Long, digits: Int): String {
    val hi = 1L shl (digits * 4)
    return java.lang.Long.toHexString(hi or (`val` and (hi - 1))).substring(1)
}

private const val KEY = "~d}\$Q7\$eIni=V)9\\RK/P.RM4;9[7|@/CA}b~OW!3?EV`:<>M7pddUBL5n|0/*Cn"

@OptIn(ExperimentalStdlibApi::class)
@Throws(Exception::class)
internal fun signature(data: String): String {
    val hmac = Mac.getInstance("HmacSHA256")
    hmac.init(SecretKeySpec(KEY.toByteArray(), "HmacSHA256"))

    return hmac.doFinal(data.toByteArray()).toHexString()
}
