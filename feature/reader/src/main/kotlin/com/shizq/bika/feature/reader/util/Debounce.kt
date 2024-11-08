package com.shizq.bika.feature.reader.util

import android.os.SystemClock
import android.util.Log

class Debounce(
    private val interval: Long,
) {
    private var lastCallTime = -1L

    operator fun invoke(block: () -> Unit) {
        val time = SystemClock.uptimeMillis()
        if (shouldInvoke(time)) {
            block()
            lastCallTime = time
            Log.d("Debounce", time.toString())
        }
    }

    private fun shouldInvoke(time: Long): Boolean {
        return time - lastCallTime > interval
    }
}
