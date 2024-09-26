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
        initListener()
    }

    open fun initData() {}

    open fun initView() {}
    open fun initListener() {}
    protected fun startActivity(
        clz: Class<*>,
        intent: Intent.() -> Unit = {},
        bundle: Bundle.() -> Unit = {}
    ) {
        startActivity(
            Intent(this, clz).apply(intent).putExtras(Bundle().apply(bundle))
        )
    }
}