package com.shizq.bika.ui.signin

import androidx.lifecycle.ViewModel
import com.shizq.bika.core.network.api.BikaNetworkApi
import com.shizq.bika.network.base.BaseHeaders
import com.shizq.bika.utils.SPUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import  com.shizq.bika.core.result.*

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: BikaNetworkApi,
) : ViewModel() {

    fun login(account: String, password: String) {
        val headers = BaseHeaders("auth/sign-in", "POST").getHeaders()
        flow { emit(api.login(account, password, headers)) }
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Error -> TODO()
                    Result.Loading -> TODO()
                    is Result.Success -> SPUtil.put("token", result.data.token)
                }
            }
    }
}
