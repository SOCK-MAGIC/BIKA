package com.shizq.bika.ui.signin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.shizq.bika.network.RetrofitUtil
import com.shizq.bika.network.base.BaseHeaders
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class LoginViewModel : ViewModel() {

    fun login(account:String,password:String) {
        val headers = BaseHeaders("auth/sign-in", "POST").getHeaders()
        val body = JsonObject().apply {
            addProperty("email", account)
            addProperty("password", password)
        }.asJsonObject.toString()
            .toRequestBody("application/json; charset=UTF-8".toMediaTypeOrNull())
        viewModelScope.launch {
            val data = RetrofitUtil.service.signInPost(body, headers).data
            Log.d("login", "login: $data")
        }
    }
}
