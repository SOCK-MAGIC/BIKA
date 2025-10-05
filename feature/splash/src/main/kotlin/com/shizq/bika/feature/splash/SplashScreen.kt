package com.shizq.bika.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    navigationToInterest: () -> Unit,
    navigationToSignIn: () -> Unit,
) {
    SideEffect {
        if (splashViewModel.hasToken) {
            navigationToInterest()
        } else {
            navigationToSignIn()
        }
    }
}
