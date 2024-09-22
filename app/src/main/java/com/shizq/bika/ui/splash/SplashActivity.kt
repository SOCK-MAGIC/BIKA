package com.shizq.bika.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shizq.bika.BR
import com.shizq.bika.R
import com.shizq.bika.base.BaseActivity
import com.shizq.bika.databinding.ActivitySplashBinding
import com.shizq.bika.ui.account.AccountActivity
import com.shizq.bika.ui.main.MainActivity
import com.shizq.bika.utils.AppVersion
import com.shizq.bika.utils.SPUtil

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    private lateinit var splashScreen: SplashScreen

    override fun initParam() {
        super.initParam()
        splashScreen = installSplashScreen()
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_splash
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        showProgressBar(true, "获取版本信息...")
        viewModel.getLatestVersion() // 版本检测
    }

    @SuppressLint("SetTextI18n")
    override fun initViewObservable() {
        viewModel.liveData_latest_version.observe(this) {
            if (it != null && it.version.toInt() > AppVersion().code()) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("新版本 v${it.short_version}")
                    .setMessage(it.release_notes)
                    .setCancelable(false)
                    .setPositiveButton("更新") { _, _ ->
                        val intent = Intent()
                        intent.action = "android.intent.action.VIEW"
                        intent.data = Uri.parse(it.download_url)
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("取消") { _, _ ->
                    }
                    .show()
            }
        }

        // 检查是否有token 没有就进行登录 显示登录提示框
        if (SPUtil.get("token", "") == "") {
            // 没有token 挑转到登录页面
            startActivity(AccountActivity::class.java)
            finish()
        } else {
            // 有token 跳转主页
            startActivity(MainActivity::class.java)
            finish()
        }
    }

    private fun showProgressBar(show: Boolean, string: String) {
        binding.loadProgressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.loadError.visibility = if (show) View.GONE else View.VISIBLE
        binding.loadText.text = string
        binding.loadLayout.isEnabled = !show
    }
}
