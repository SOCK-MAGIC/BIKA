package com.shizq.bika.ui.chatroom.current

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shizq.bika.base.BaseViewModel
import com.shizq.bika.bean.ChatMessageBean
import com.shizq.bika.bean.UserMention
import com.shizq.bika.network.RetrofitUtil
import com.shizq.bika.network.base.BaseHeaders
import com.shizq.bika.utils.SPUtil
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.util.*

class ChatRoomViewModel : BaseViewModel() {
    var roomId = ""

    var replyMessage: String = ""
    var replyId: String = ""
    var replyName: String = ""
    var replyImage: String = ""
    var replyMessageType: String = ""

    val liveData_message: MutableLiveData<ChatMessageBean> by lazy {
        MutableLiveData<ChatMessageBean>()
    }

    val liveDataSendMessage: MutableLiveData<ChatMessageBean> by lazy {
        MutableLiveData<ChatMessageBean>()
    }

    //发送文字消息 当前不能@不能回复
    fun sendMessage(message: String, userMentions: List<UserMention> = listOf()) {
        val referenceId = UUID.randomUUID().toString()
        //预先添加个数据 等发送成功再进行数据和ui的更新
        liveData_message.postValue(
            Gson().fromJson(
                myMessage(message, "", referenceId, userMentions),
                ChatMessageBean::class.java
            )
        )
        val reply = mutableMapOf(
            "name" to replyName,
            "id" to replyId,
            "type" to replyMessageType,
        )

        when (replyMessageType) {
            "IMAGE_MESSAGE" -> {
                reply["image"] = replyImage
            }
            else -> {
                reply["message"] = replyMessage
            }
        }

        val map = mutableMapOf(
            "roomId" to roomId,
            "message" to message,
            "referenceId" to referenceId,
            "userMentions" to userMentions
        )

        if (replyName != "") {
            map["reply"] = reply
        }

        val body = Gson().toJson(map)
            .toRequestBody("application/json; charset=UTF-8".toMediaType())
        val headers = BaseHeaders().getChatHeaderMapAndToken()
        RetrofitUtil.service_live.chatSendMessagePost(body, headers)
            .doOnSubscribe(this)
            .subscribe(object : Observer<ChatMessageBean> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    var t: ChatMessageBean? = null
                    if (e is HttpException) {   //  处理服务器返回的非成功异常
                        val responseBody = e.response()!!.errorBody()
                        if (responseBody != null) {
                            val type = object : TypeToken<ChatMessageBean>() {}.type
                            t = Gson().fromJson(responseBody.string(), type)
                            liveDataSendMessage.postValue(t)
                        } else {
                            liveDataSendMessage.postValue(t)
                        }
                    }
                }

                override fun onComplete() {

                }

                override fun onNext(t: ChatMessageBean) {
                    liveDataSendMessage.postValue(t)
                }

            })
    }

    //发送图片 当前只能发送单张
    fun sendImage(message: String, path: String, userMentions: List<UserMention> = listOf()) {
        val referenceId = UUID.randomUUID().toString()
        //预先添加个数据 等发送成功再进行数据和ui的更新
        liveData_message.postValue(
            Gson().fromJson(
                myMessage(message, path, referenceId, userMentions),
                ChatMessageBean::class.java
            )
        )
        val multipartBody =
            MultipartBody.Builder()
                .setType("multipart/form-data".toMediaType())
        multipartBody.addFormDataPart("roomId", roomId)
        multipartBody.addFormDataPart("referenceId", referenceId)

        val captionHeaders = Headers.Builder()
            .addUnsafeNonAscii("content-disposition", "form-data; name=caption")
            .addUnsafeNonAscii("content-transfer-encoding", "binary")
            .build()
        multipartBody.addPart(
            MultipartBody.Part.create(
                captionHeaders,
                message.toRequestBody("text/plain; charset=utf-8".toMediaType())
            )
        )

        multipartBody.addFormDataPart("userMentions", userMentions.toString())

        //图片后面可能for循环添加
        val file = File(path)
        val body = file.asRequestBody("application/octet-stream".toMediaType())
        multipartBody.addFormDataPart("images", file.name, body)

        val multipartBodyBuild = multipartBody.build()
        val headers = BaseHeaders().getChatHeaderMapAndToken()
        headers["content-type"] = "multipart/form-data; boundary=${multipartBodyBuild.boundary}"

        RetrofitUtil.service_live.chatSendImagePost(headers, multipartBodyBuild)
            .doOnSubscribe(this@ChatRoomViewModel)
            .subscribe(object : Observer<ChatMessageBean> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    var t: ChatMessageBean? = null
                    if (e is HttpException) {   //  处理服务器返回的非成功异常
                        val responseBody = e.response()!!.errorBody()
                        if (responseBody != null) {
                            val type = object : TypeToken<ChatMessageBean>() {}.type
                            t = Gson().fromJson(responseBody.string(), type)
                            liveDataSendMessage.postValue(t)
                        } else {
                            liveDataSendMessage.postValue(t)
                        }
                    }
                }

                override fun onComplete() {

                }

                override fun onNext(t: ChatMessageBean) {
                    liveDataSendMessage.postValue(t)
                }

            })
    }

    //非服务器返回的数据 用于显示ui的数据
    fun myMessage(
        message: String,
        imagePath: String = "",
        referenceId: String,
        userMentions: List<UserMention> = listOf()
    ): String {
        val fileServer = SPUtil.get("user_fileServer", "") as String
        val path = SPUtil.get("user_path", "") as String
        val avatarUrl =
            if (fileServer == "") "" else "${fileServer.replace("/static/", "")}/static/$path"

        val messageMap = if (imagePath == "") {
            mutableMapOf(
                "message" to message
            )
        } else {
            mutableMapOf(
                "caption" to message,
                "medias" to listOf(imagePath)
            )
        }

        val profile = mutableMapOf(
            "name" to SPUtil.get("user_name", "") as String,
            "level" to SPUtil.get("user_level", 1) as Int,
            "characters" to listOf<String>(),
            "avatarUrl" to avatarUrl,
        )
        val data = mutableMapOf(
            "message" to messageMap,
            "profile" to profile,
            "userMentions" to userMentions
        )

        val json = if (imagePath == "") {
            mutableMapOf(
                "referenceId" to referenceId,
                "type" to "TEXT_MESSAGE",
                "isBlocked" to false,
                "data" to data
            )
        } else {
            mutableMapOf(
                "referenceId" to referenceId,
                "type" to "IMAGE_MESSAGE",
                "isBlocked" to false,
                "data" to data
            )
        }
        return Gson().toJson(json)
    }
}