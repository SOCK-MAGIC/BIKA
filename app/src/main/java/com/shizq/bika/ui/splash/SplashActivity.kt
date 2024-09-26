package com.shizq.bika.ui.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.shizq.bika.R
import com.shizq.bika.base.BaseActivity2
import com.shizq.bika.ui.main.MainActivity
import com.shizq.bika.ui.signin.SignInActivity
import com.shizq.bika.ui.splash.SplashActivityUiState.Failed
import com.shizq.bika.ui.splash.SplashActivityUiState.Loading
import com.shizq.bika.ui.splash.SplashActivityUiState.Success
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : BaseActivity2() {
    override val layoutId: Int = R.layout.activity_splash
    private val viewModel: SplashActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .collect {
                        when (it) {
                            Loading -> Unit
                            is Success -> {
                                startActivity(MainActivity::class.java)
                                finish()
                            }

                            Failed -> {
                                startActivity(SignInActivity::class.java)
                                finish()
                            }
                        }
                    }
            }
        }
    }
}
