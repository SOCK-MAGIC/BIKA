package com.shizq.bika

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.shizq.bika.core.log.Logger
import com.shizq.bika.sync.initializers.Sync
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltAndroidApp
class BikaApplication :
    Application(),
    SingletonImageLoader.Factory {
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    @Inject
    lateinit var client: dagger.Lazy<OkHttpClient>

    override fun onCreate() {
        super.onCreate()

        Sync.initialize(this)
        Logger.initialize()
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader = imageLoader.get()
}
