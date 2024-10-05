package com.shizq.bika.feature.signin

data class SignInViewState(val account: String = "", val password: String = "")

data class UserCredential(val email: String , val password: String )
