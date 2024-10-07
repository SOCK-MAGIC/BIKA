package com.shizq.bika.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun SplashScreen(
    component: SplashComponent,
    navigationToInterest: () -> Unit,
    navigationToSignIn: () -> Unit
) {
    SideEffect {
        if (component.hasToken) {
            navigationToInterest()
        } else {
            navigationToSignIn()
        }
    }
}
