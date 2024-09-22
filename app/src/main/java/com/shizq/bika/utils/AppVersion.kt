package com.shizq.bika.utils

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.shizq.bika.BIKAApplication

class AppVersion {
    fun code(): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getPackageInfo().longVersionCode
        } else {
            @Suppress("DEPRECATION")
            getPackageInfo().versionCode.toLong()
        }
    }

    fun name(): String? {
        return getPackageInfo().versionName
    }

    private fun getPackageInfo(): PackageInfo {
        val packageManager = BIKAApplication.contextBase.packageManager
        val packageName = BIKAApplication.contextBase.packageName
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }
    }
}