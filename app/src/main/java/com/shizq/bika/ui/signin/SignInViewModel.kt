package com.shizq.bika.ui.signin

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.google.gson.JsonObject
import com.shizq.bika.R
import com.shizq.bika.base.BaseViewModel
import com.shizq.bika.bean.SignInBean
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.result.Result
import com.shizq.bika.core.result.asResult
import com.shizq.bika.network.RetrofitUtil
import com.shizq.bika.network.base.BaseHeaders
import com.shizq.bika.network.base.BaseObserver
import com.shizq.bika.network.base.BaseResponse
import com.shizq.bika.utils.SPUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val network: BikaNetworkDataSource,
    private val preferencesDataSource: BikaPreferencesDataSource
) : BaseViewModel() {

    var forgot_answer: String? = null
    var forgot_email: String? = null
    var forgot_questionNo: String? = null
    val email: ObservableField<String> = ObservableField(SPUtil.get<String>("username", ""))
    val password: ObservableField<String> = ObservableField(SPUtil.get<String>("password", ""))

    val liveData_signin: MutableLiveData<BaseResponse<SignInBean>> by lazy {
        MutableLiveData<BaseResponse<SignInBean>>()
    }

    val liveData_forgot: MutableLiveData<BaseResponse<SignInBean>> by lazy {
        MutableLiveData<BaseResponse<SignInBean>>()
    }

    val liveData_password: MutableLiveData<BaseResponse<SignInBean>> by lazy {
        MutableLiveData<BaseResponse<SignInBean>>()
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            flow { emit(network.signIn(email, password)) }
                .asResult()
                .map { result ->
                    when (result) {
                        is Result.Error -> onError(result.exception)
                        Result.Loading -> Unit
                        is Result.Success -> {
                            onSuccess()
                            preferencesDataSource.setToken(result.data.token)
                            SPUtil.put("token", result.data.token)
                            SPUtil.put("username", email)
                            SPUtil.put("password", password)
                        }
                    }
                }
                .collect()
        }
    }

    fun getForgot() {
        val body = JsonObject().apply { addProperty("email", forgot_email) }.asJsonObject.toString()
            .toRequestBody("application/json; charset=UTF-8".toMediaTypeOrNull())
        val headers = BaseHeaders("auth/forgot-password", "POST").getHeaders()

        RetrofitUtil.service.forgotPasswordPost(body, headers)
            .doOnSubscribe(this@SignInViewModel)
            .subscribe(object : BaseObserver<SignInBean>() {
                override fun onSuccess(baseResponse: BaseResponse<SignInBean>) {
                    liveData_forgot.postValue(baseResponse)
                }

                override fun onCodeError(baseResponse: BaseResponse<SignInBean>) {
                    liveData_forgot.postValue(baseResponse)
                }
            })
    }

    fun resetPassword() {
        val body = JsonObject().apply {
            addProperty("answer", forgot_answer)
            addProperty("email", forgot_email)
            addProperty("questionNo", forgot_questionNo)
        }.asJsonObject.toString()
            .toRequestBody("application/json; charset=UTF-8".toMediaTypeOrNull())
        val headers = BaseHeaders("auth/reset-password", "POST").getHeaders()

        RetrofitUtil.service.resetPasswordPost(body, headers)
            .doOnSubscribe(this@SignInViewModel)
            .subscribe(object : BaseObserver<SignInBean>() {

                override fun onSuccess(baseResponse: BaseResponse<SignInBean>) {
                    liveData_password.postValue(baseResponse)
                }

                override fun onCodeError(baseResponse: BaseResponse<SignInBean>) {
                    liveData_password.postValue(baseResponse)
                }
            })
    }
}
