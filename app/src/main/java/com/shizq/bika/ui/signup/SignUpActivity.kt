package com.shizq.bika.ui.signup

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shizq.bika.R
import com.shizq.bika.base.BaseActivity2
import com.shizq.bika.databinding.FragmentSignupBinding
import com.shizq.bika.utils.bindView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity2() {
    override val layoutId: Int = R.layout.fragment_signup
    private val binding = bindView<FragmentSignupBinding>(R.layout.fragment_signup)
    private val viewModel by viewModels<SignUpViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
//        supportActionBar?.title = "注册"
    }
}