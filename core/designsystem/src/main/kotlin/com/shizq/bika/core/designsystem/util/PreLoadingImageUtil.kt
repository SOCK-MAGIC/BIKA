package com.shizq.bika.core.designsystem.util

import android.content.Context
import android.util.Log
import coil3.imageLoader
import coil3.request.ImageRequest

fun preLoading(context: Context, imageUrl: String) {
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .listener(
            onStart = {
                Log.d("preloading", imageUrl)
            },
        ) { _, _ -> }
        .build()
    context.imageLoader.enqueue(request)
}
