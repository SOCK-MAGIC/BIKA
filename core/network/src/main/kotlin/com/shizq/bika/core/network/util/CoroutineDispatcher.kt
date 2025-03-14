package com.shizq.bika.core.network.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

fun CoroutineDispatcher.asExecutorService(): ExecutorService =
    object : Executor by asExecutor(), AbstractExecutorService() {
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
