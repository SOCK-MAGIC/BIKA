package com.shizq.bika.network.base

abstract class BaseObserver<T> : BaseResponse<T>() {

    //请求成功且返回码为200的回调方法，这里抽象方法声明
    abstract fun onSuccess(baseResponse: BaseResponse<T>)

    //请求成功但返回code码不是200的回调方法，这里抽象方法声明
    abstract fun onCodeError(baseResponse: BaseResponse<T>)
    /**
     *
     * 请求失败回调方法，这里抽象方法声明
     * @param isCauseNetReason 是否由于网络造成的失败 true是
     */
    /*
    public abstract void onFailure(Throwable throwable, boolean isCauseNetReason) throws Exception;
    */
}
