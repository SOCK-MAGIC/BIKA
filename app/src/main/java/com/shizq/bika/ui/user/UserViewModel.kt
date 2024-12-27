package com.shizq.bika.ui.user

import androidx.lifecycle.MutableLiveData
import com.shizq.bika.base.BaseViewModel
import com.shizq.bika.bean.ProfileBean
import com.shizq.bika.network.base.BaseHeaders
import com.shizq.bika.network.base.BaseResponse

class UserViewModel : BaseViewModel() {
    val liveData_avatar: MutableLiveData<BaseResponse<Any>> by lazy {
        MutableLiveData<BaseResponse<Any>>()
    }

    val liveDataSlogan: MutableLiveData<BaseResponse<Any>> by lazy {
        MutableLiveData<BaseResponse<Any>>()
    }

    val liveData_profile: MutableLiveData<BaseResponse<ProfileBean>> by lazy {
        MutableLiveData<BaseResponse<ProfileBean>>()
    }

    fun putAvatar(base64Image: String) {
        val map = mutableMapOf(
            "avatar" to base64Image
        )
        // val body = RequestBody.create(
        //     "application/json; charset=UTF-8".toMediaTypeOrNull(),
        //     Gson().toJson(map)
        // )
        // val headers = BaseHeaders("users/avatar", "PUT").getHeaderMapAndToken()
        // RetrofitUtil.service.avatarPUT(body, headers)
        //     .doOnSubscribe(this)
        //     .subscribe(object : BaseObserver<Any>() {
        //         override fun onSuccess(baseResponse: BaseResponse<Any>) {
        //             liveData_avatar.postValue(baseResponse)
        //         }
        //
        //         override fun onCodeError(baseResponse: BaseResponse<Any>) {
        //             liveData_avatar.postValue(baseResponse)
        //         }
        //     })
    }

    fun putProfile(slogan: String) {
        val map = mutableMapOf(
            "slogan" to slogan
        )
        val headers = BaseHeaders("users/profile", "PUT").getHeaderMapAndToken()
    }

    fun getProfile() {

    }
}