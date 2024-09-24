package com.shizq.bika.base

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity2 : AppCompatActivity() {
    @get:LayoutRes
    abstract val layoutId: Int
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layoutId)
        initView()
        initData()
    }

    open fun initData() {}

    open fun initView() {}
    protected fun startActivity(clz: Class<*>, bundle: Bundle.() -> Unit = {}) {
        startActivity(Intent(this, clz).putExtras(Bundle().apply(bundle)))
    }
}