package com.shizq.bika.feature.signin

sealed interface LoginViewEvent {
    data object NavTo : LoginViewEvent
    data class ShowMessage(val message: String) : LoginViewEvent
}
