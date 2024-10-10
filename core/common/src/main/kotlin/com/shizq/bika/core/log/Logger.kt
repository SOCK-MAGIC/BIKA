package com.shizq.bika.core.log

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object Logger {
    fun initialize() {
        Napier.base(DebugAntilog())
    }
}
