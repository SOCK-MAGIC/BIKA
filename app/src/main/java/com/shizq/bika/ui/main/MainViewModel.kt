package com.shizq.bika.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.shizq.bika.BIKAApplication
import com.shizq.bika.R
import com.shizq.bika.base.BaseViewModel
import com.shizq.bika.bean.CategoriesBean
import com.shizq.bika.bean.ProfileBean
import com.shizq.bika.bean.PunchInBean
import com.shizq.bika.bean.SignInBean
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.network.RetrofitUtil
import com.shizq.bika.network.base.BaseHeaders
import com.shizq.bika.network.base.BaseObserver
import com.shizq.bika.network.base.BaseResponse
import com.shizq.bika.utils.SPUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(
    private val network: BikaNetworkDataSource
) : BaseViewModel() {
    var userId = "" // 用来确认账号是否已经登录
    var fileServer = ""
    var path = ""
    private val cTitle = arrayOf(
        R.string.categories_recommend,
        R.string.categories_leaderboard,
        R.string.categories_game,
        R.string.categories_apps,
//        R.string.categories_chat,
        R.string.categories_forum,
        R.string.categories_latest,
        R.string.categories_random
    )
    private val cRes = arrayOf(
        R.drawable.bika,
        R.drawable.cat_leaderboard,
        R.drawable.cat_game,
        R.drawable.cat_love_pica,
//        R.drawable.ic_chat,
        R.drawable.cat_forum,
        R.drawable.cat_latest,
        R.drawable.cat_random
    )
    val liveData_profile: MutableLiveData<BaseResponse<ProfileBean>> by lazy {
        MutableLiveData<BaseResponse<ProfileBean>>()
    }

    val liveData_punch_in: MutableLiveData<BaseResponse<PunchInBean>> by lazy {
        MutableLiveData<BaseResponse<PunchInBean>>()
    }

    val liveData_signin: MutableLiveData<BaseResponse<SignInBean>> by lazy {
        MutableLiveData<BaseResponse<SignInBean>>()
    }

    val liveData: MutableLiveData<BaseResponse<CategoriesBean>> by lazy {
        MutableLiveData<BaseResponse<CategoriesBean>>()
    }

    var categoriesList = ArrayList<CategoriesBean.Category>()

    var cList = {
        val categoriesList = ArrayList<CategoriesBean.Category>()
        for (index in cTitle.indices) {
            val category = CategoriesBean.Category(
                "",
                false,
                "",
                false,
                "",
                thumb = CategoriesBean.Category.Thumb("", "", ""),
                BIKAApplication.contextBase.resources.getString(cTitle[index]),
                cRes[index]
            )
            categoriesList.add(index, category)
        }
        categoriesList
    }

    fun getProfile() {
        RetrofitUtil.service.profileGet(BaseHeaders("users/profile", "GET").getHeaderMapAndToken())
            .doOnSubscribe(this@MainViewModel)
            .subscribe(object : BaseObserver<ProfileBean>() {
                override fun onSuccess(baseResponse: BaseResponse<ProfileBean>) {
                    // 请求成功
                    liveData_profile.postValue(baseResponse)
                }

                override fun onCodeError(baseResponse: BaseResponse<ProfileBean>) {
                    liveData_profile.postValue(baseResponse)
                }
            })
    }

    fun getSignIn() {
        val body = JsonObject().apply {
            addProperty("email", SPUtil.get("username", "") as String)
            addProperty("password", SPUtil.get("password", "") as String)
        }.asJsonObject.toString()
            .toRequestBody("application/json; charset=UTF-8".toMediaTypeOrNull())
        val headers = BaseHeaders("auth/sign-in", "POST").getHeaders()
        viewModelScope.launch {
            RetrofitUtil.service.signInPost(body, headers).let {
                liveData_signin.postValue(it)
            }
        }
    }

    fun getCategories() {
        val headers = BaseHeaders("categories", "GET").getHeaderMapAndToken()
        RetrofitUtil.service.categoriesGet(headers)
            .doOnSubscribe(this@MainViewModel)
            .subscribe(object : BaseObserver<CategoriesBean>() {
                override fun onSuccess(baseResponse: BaseResponse<CategoriesBean>) {
                    liveData.postValue(baseResponse)
                }

                override fun onCodeError(baseResponse: BaseResponse<CategoriesBean>) {
                    liveData.postValue(baseResponse)
                }
            })
    }
}
