package com.shizq.bika.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shizq.bika.core.network.api.BikaNetworkApi
import com.shizq.bika.network.base.BaseHeaders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: BikaNetworkApi,
) : ViewModel() {

    fun login(account: String, password: String) {
        val headers = BaseHeaders("auth/sign-in", "POST").getHeaders()
        viewModelScope.launch {
            (api.login(account, password))
        }
//        flow { emit(api.login(account, password, headers)) }
//            .asResult()
//            .map { result ->
//                when (result) {
//                    is Result.Error -> TODO()
//                    Result.Loading -> TODO()
//                    is Result.Success -> SPUtil.put("token", result.data.token)
//                }
//            }
    }
}
