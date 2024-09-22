package com.shizq.bika.ui.splash

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.shizq.bika.base.BaseViewModel
import com.shizq.bika.bean.UpdateBean
import com.shizq.bika.network.RetrofitUtil
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class SplashViewModel(application: Application) : BaseViewModel(application) {
    val liveData_latest_version: MutableLiveData<UpdateBean> = MutableLiveData<UpdateBean>()

    fun getLatestVersion() {
        RetrofitUtil.service_update.updateGet()
            .doOnSubscribe(this@SplashViewModel)
            .subscribe(object : Observer<UpdateBean> {
                override fun onNext(t: UpdateBean) {
                    liveData_latest_version.postValue(t)
                }

                override fun onError(e: Throwable) {}
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
            })
    }
}
