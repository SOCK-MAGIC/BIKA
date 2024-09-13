package com.shizq.bika.core.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val bikaDispatcher: BikaDispatchers)

enum class BikaDispatchers {
    Default,
    IO,
}