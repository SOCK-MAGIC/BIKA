package com.shizq.bika.ui.notifications

import androidx.lifecycle.MutableLiveData
import com.shizq.bika.base.BaseViewModel
import com.shizq.bika.bean.NotificationsBean
import com.shizq.bika.network.base.BaseResponse

class NotificationsViewModel  : BaseViewModel() {
    var page = 0

    val liveData: MutableLiveData<BaseResponse<NotificationsBean>> by lazy {
        MutableLiveData<BaseResponse<NotificationsBean>>()
    }

    fun getNotifications() {
        page++
        // RetrofitUtil.service.notificationsGet(
        //     page.toString(),
        //     BaseHeaders("users/notifications?page=$page", "GET").getHeaderMapAndToken()
        // )
        //     .doOnSubscribe(this)
        //     .subscribe(object : BaseObserver<NotificationsBean>() {
        //         override fun onSuccess(baseResponse: BaseResponse<NotificationsBean>) {
        //             liveData.postValue(baseResponse)
        //         }
        //
        //         override fun onCodeError(baseResponse: BaseResponse<NotificationsBean>) {
        //             page--
        //             liveData.postValue(baseResponse)
        //         }
        //
        //     })
    }
}