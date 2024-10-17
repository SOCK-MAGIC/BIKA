package com.shizq.bika.core.network.pagingsource

import android.content.Context
import coil3.imageLoader
import coil3.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreLoading @Inject constructor(@ApplicationContext private val appContext: Context) {
    init {
        val request = ImageRequest.Builder(appContext)
            .data("")
            .memoryCacheKey("")
            .build()
        appContext.imageLoader.enqueue(request)
    }
}