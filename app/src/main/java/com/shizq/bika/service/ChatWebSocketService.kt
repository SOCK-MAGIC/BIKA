package com.shizq.bika.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.shizq.bika.bean.ChatMessageBean
import com.shizq.bika.network.RetrofitUtil
import com.shizq.bika.network.websocket.ChatWebSocketManager
import com.shizq.bika.network.websocket.IReceiveMessage
import com.shizq.bika.utils.SPUtil

//新聊天室Service
class ChatWebSocketService : Service() {
    private val binder = ChatBinder()
    lateinit var webSocketManager: ChatWebSocketManager

    val liveData_message: MutableLiveData<ChatMessageBean> by lazy {
        MutableLiveData<ChatMessageBean>()
    }
    val liveData_state: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        webSocketManager = ChatWebSocketManager.getInstance()
    }

    override fun onUnbind(intent: Intent): Boolean {
        webSocketManager.close()
        return super.onUnbind(intent)
    }

    inner class ChatBinder : Binder() {
        fun getService(): ChatWebSocketService = this@ChatWebSocketService
    }

    fun webSocket(roomId:String) {
        val url = RetrofitUtil.LIVE_SERVER +
                "/?token=" +
                SPUtil.get("chat_token", "") as String +
                "&room=" + roomId
        webSocketManager.init(url, object :
            IReceiveMessage {
            override fun onConnectSuccess() {}

            override fun onConnectFailed() {
                liveData_state.postValue("failed")
            }

            override fun onClose() {
                liveData_state.postValue("close")
            }

            override fun onMessage(text: String) {
                liveData_state.postValue("success")

                // val bean = Gson().fromJson(text, ChatMessageBean::class.java)
                // if (bean.data != null) {
                //     if (bean.type == "TEXT_MESSAGE" || bean.type == "IMAGE_MESSAGE") {
                //         //防止重复显示 屏蔽掉自己发送的消息
                //         if (bean.data.profile.name !=
                //             SPUtil.get("user_name", "") as String
                //         ) {
                //             liveData_message.postValue(bean)
                //         }
                //     } else {
                //         liveData_message.postValue(bean)
                //     }
                // }
            }
        })
    }
}