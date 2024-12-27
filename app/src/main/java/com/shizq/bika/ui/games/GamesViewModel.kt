package com.shizq.bika.ui.games

import androidx.lifecycle.MutableLiveData
import com.shizq.bika.base.BaseViewModel
import com.shizq.bika.bean.GamesBean
import com.shizq.bika.network.base.BaseResponse

class GamesViewModel : BaseViewModel() {
    var page = 0//当前页数

    val liveData: MutableLiveData<BaseResponse<GamesBean>> by lazy {
        MutableLiveData<BaseResponse<GamesBean>>()
    }

    fun getGames() {
        page++//每次请求加1
        // RetrofitUtil.service.gamesGet(
        //     page.toString(),
        //     BaseHeaders("games?page=$page", "GET").getHeaderMapAndToken()
        // )
        //     .doOnSubscribe(this)
        //     .subscribe(object : BaseObserver<GamesBean>(){
        //         override fun onSuccess(baseResponse: BaseResponse<GamesBean>) {
        //             liveData.postValue(baseResponse)
        //         }
        //
        //         override fun onCodeError(baseResponse: BaseResponse<GamesBean>) {
        //             page--
        //             liveData.postValue(baseResponse )
        //         }
        //     })
    }

}

