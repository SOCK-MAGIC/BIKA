package com.shizq.bika

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.google.android.material.color.DynamicColors
import com.shizq.bika.core.log.Logger
import com.shizq.bika.sync.initializers.Sync
import com.shizq.bika.utils.SPUtil
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltAndroidApp
class BIKAApplication :
    Application(),
    SingletonImageLoader.Factory {
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
        DynamicColors.applyToActivitiesIfAvailable(this) // 根据壁纸修改App主题颜色

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

        Sync.initialize(this)
        Logger.initialize()
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader = imageLoader.get()
}
