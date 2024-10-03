package com.shizq.bika.core.network.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

internal const val API_KEY = "C69BAF41DA5ABD1FFEDC6D2FEA56B"
internal const val DIGEST_KEY = "~d}\$Q7\$eIni=V)9\\RK/P.RM4;9[7|@/CA}b~OW!3?EV`:<>M7pddUBL5n|0/*Cn"
internal const val PICA_API = "https://picaapi.picacomic.com/"

@OptIn(ExperimentalStdlibApi::class)
internal fun signature(key: String, text: String): String {
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(SecretKeySpec(key.toByteArray(), "HmacSHA256"))
    return mac.doFinal(text.toByteArray()).toHexString()
}

fun CoroutineDispatcher.asExecutorService(): ExecutorService = object : Executor by asExecutor(), AbstractExecutorService() {
    override fun shutdown() {
        throw UnsupportedOperationException("shutdown is not implemented")
    }

    override fun shutdownNow(): MutableList<Runnable> {
        throw UnsupportedOperationException("shutdownNow is not implemented")
    }

    override fun isShutdown(): Boolean = false

    override fun isTerminated(): Boolean = false

    override fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean = false
}