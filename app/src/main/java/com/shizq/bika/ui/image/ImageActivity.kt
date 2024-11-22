package com.shizq.bika.ui.image

import android.os.Bundle
import androidx.core.view.ViewCompat
import com.shizq.bika.R
import com.shizq.bika.base.BaseActivity
import com.shizq.bika.databinding.ActivityImageBinding

//图片展示
class ImageActivity : BaseActivity<ActivityImageBinding, ImageViewModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_image
    }

    override fun initVariableId(): Int {
        return 1
    }

    override fun initData() {
        ViewCompat.setTransitionName(binding.touchImageView,"image")
        val fileserver = intent.getStringExtra("fileserver") as String
        val imageurl = intent.getStringExtra("imageurl") as String

        binding.touchImageView.setOnClickListener {
            finishAfterTransition()
        }
    }
}