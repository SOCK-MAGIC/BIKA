package com.shizq.bika

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.Uri
import coil3.annotation.InternalCoilApi
import coil3.fetch.Fetcher
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.util.CoilUtils
import coil3.util.FetcherServiceLoaderTarget
import coil3.util.ServiceLoaderComponentRegistry
import com.google.android.material.color.DynamicColors
import com.shizq.bika.sync.initializers.Sync
import com.shizq.bika.utils.SPUtil
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import javax.inject.Inject
import kotlin.reflect.KClass

@OptIn(InternalCoilApi::class)
@HiltAndroidApp
class BIKAApplication : Application(), SingletonImageLoader.Factory ,
    FetcherServiceLoaderTarget<Uri> {
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    @Inject
    lateinit var client: dagger.Lazy<OkHttpClient>

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var contextBase: Application
    }


    override fun onCreate() {
        super.onCreate()
        contextBase = this
        DynamicColors.applyToActivitiesIfAvailable(this)// 根据壁纸修改App主题颜色

        SPUtil.init(this)
        val nightMode = SPUtil.get("setting_night", "跟随系统") as String
        AppCompatDelegate.setDefaultNightMode(
            when (nightMode) {
                "开启" -> AppCompatDelegate.MODE_NIGHT_YES
                "关闭" -> AppCompatDelegate.MODE_NIGHT_NO
                "跟随系统" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                else -> AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        // Sync.initialize(this)
        OkHttpNetworkFetcherFactory { client.get() }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader = imageLoader.get()
    override fun factory(): Fetcher.Factory<Uri>? {
        TODO("Not yet implemented")
    }

    override fun type(): KClass<Uri>? {
        TODO("Not yet implemented")
    }
}