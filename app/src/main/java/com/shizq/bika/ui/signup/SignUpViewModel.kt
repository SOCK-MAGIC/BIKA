package com.shizq.bika.ui.signup

import androidx.lifecycle.MutableLiveData
import com.shizq.bika.base.BaseViewModel
import com.shizq.bika.bean.SignInBean
import com.shizq.bika.network.base.BaseHeaders
import com.shizq.bika.network.base.BaseResponse

class SignUpViewModel : BaseViewModel() {

    val liveData_signup: MutableLiveData<BaseResponse<SignInBean>> by lazy {
        MutableLiveData<BaseResponse<SignInBean>>()
    }

    fun requestSignUp() {
//        Log.d("---json---",json.toString())

        val headers = BaseHeaders("auth/register", "POST").getHeaders()
    }
}