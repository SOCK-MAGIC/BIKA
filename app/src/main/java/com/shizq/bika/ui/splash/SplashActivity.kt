package com.shizq.bika.ui.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.shizq.bika.R
import com.shizq.bika.base.BaseActivity2
import com.shizq.bika.ui.account.AccountActivity
import com.shizq.bika.ui.main.MainActivity
import com.shizq.bika.utils.SPUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.shizq.bika.ui.splash.SplashActivityUiState.*

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
                                startActivity(AccountActivity::class.java)
                                finish()
                            }
                        }
                    }
            }
        }
    }
}
