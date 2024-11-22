package com.shizq.bika.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.shizq.bika.bean.ChatMessageOldBean
import com.shizq.bika.network.websocket.IReceiveMessage
import com.shizq.bika.network.websocket.WebSocketManager
import com.shizq.bika.utils.SPUtil

//旧聊天室service
class ChatWebSocketServiceOld : Service() {
    private val binder = ChatBinder()
    lateinit var webSocketManager: WebSocketManager

    val liveData_connections: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val liveData_message: MutableLiveData<ChatMessageOldBean> by lazy {
        MutableLiveData<ChatMessageOldBean>()
    }
    val liveData_state: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        webSocketManager = WebSocketManager.getInstance()
    }

    override fun onUnbind(intent: Intent): Boolean {
        webSocketManager.close()
        return super.onUnbind(intent)
    }

    inner class ChatBinder : Binder() {
        fun getService(): ChatWebSocketServiceOld = this@ChatWebSocketServiceOld
    }

    fun WebSocket(url: String) {
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
                if (text == "40") {
                    liveData_state.postValue("success")

                    val array = ArrayList<String>()
                    array.add("init")
                    // array.add(Gson().toJson(init()))

                    // webSocketManager.sendMessage("42" + Gson().toJson(array))
                }
                if (text.substring(0, 2) == "42") {
                    //收到消息 42 进行解析
                    // val key = JsonParser.parseString(text.substring(2)).asJsonArray[0].asString
                    // val json = JsonParser.parseString(text.substring(2)).asJsonArray[1].asJsonObject

                    when ("key") {
                        //broadcast_ads是广告
                        "broadcast_ads" -> {}
                        "new_connection" -> {
                            // liveData_connections.postValue("${json.get("connections").asString}人在线")
                        }
                        "receive_notification" -> {
                        }
                        "broadcast_message" -> {
                        }
                        "broadcast_image" -> {
                        }
                        "broadcast_audio" -> {
                        }

                        "connection_close" -> {
                            // liveData_connections.postValue("${json.get("connections").asString}人在线")
                        }

                        else -> {

                        }
                    }

                }
            }

        })
    }

    //发送文字 可以回复 可以@
    //发送图片 不可以发送文字 可以回复 可以@
    //发送语音 不可以发送文字 不可以回复 不可以@
    //悄悄话 不能发图片和语音
    fun sendMessage(
        atname: String = "",
        reply: String = "",
        reply_name: String = "",
        text: String = "",
        base64Image: String = "",
        base64Audio: String = ""
    ) {
        val fileServer = SPUtil.get("user_fileServer", "") as String
        val path = SPUtil.get("user_path", "") as String
        val character = SPUtil.get("user_character", "") as String

        val map = mutableMapOf<String, Any>()
        map["at"] = if (base64Audio == "") atname else ""
        map["audio"] = base64Audio
        if (path != "") {
            map["avatar"] = "${fileServer.replace("/static/", "")}/static/$path"
        }
        map["block_user_id"] = ""
        if (character != "") {
            map["character"] = character
        }

        map["email"] = SPUtil.get("username", "") as String
        map["gender"] = SPUtil.get("user_gender", "bot") as String
        map["image"] = base64Image
        map["level"] = SPUtil.get("user_level", 1) as Int
        map["message"] = text
        map["name"] = SPUtil.get("user_name", "") as String
        map["platform"] = "android"
        map["reply"] = if (base64Audio == "") reply else ""
        map["reply_name"] = if (base64Audio == "") reply_name else ""
        map["title"] = SPUtil.get("user_title", "") as String
        map["type"] = if (base64Image != "") 4 else if (base64Audio != "") 5 else 3
        map["unique_id"] = ""
        map["user_id"] = SPUtil.get("user_id", "") as String
        map["verified"] = SPUtil.get("user_verified", false) as Boolean

        // val json = Gson().toJson(map)
        val array = ArrayList<String>()
        array.add(if (base64Image != "") "send_image" else if (base64Audio != "") "send_audio" else "send_message")
        // array.add(json)
        // liveData_message.postValue(Gson().fromJson(json, ChatMessageOldBean::class.java))

        // Log.d("------", Gson().toJson(array))
        // webSocketManager.sendMessage("42" + Gson().toJson(array))
    }

    var init = {
        val fileServer = SPUtil.get("user_fileServer", "") as String
        val path = SPUtil.get("user_path", "") as String
        val character = SPUtil.get("user_character", "") as String

        val map = mutableMapOf<String, Any>()

        if (fileServer != "" && path != "") {
            val avatarMap = mutableMapOf<String, String>()
            avatarMap["fileServer"] = fileServer
            avatarMap["originalName"] = "avatar.jpg"
            avatarMap["path"] = path
            map["avatar"] = avatarMap
        }
        map["birthday"] = SPUtil.get("user_birthday", "") as String
        if (character != "") {
            map["character"] = character
        }
        map["characters"] = ArrayList<Any>()
        map["email"] = SPUtil.get("username", "") as String
        map["exp"] = SPUtil.get("user_exp", 0) as Int
        map["gender"] = SPUtil.get("user_gender", "bot") as String
        map["isPunched"] = SPUtil.get("setting_punch", false)
        map["level"] = SPUtil.get("user_level", 1) as Int
        map["name"] = SPUtil.get("user_name", "") as String
        map["slogan"] = SPUtil.get("user_slogan", "") as String
        map["title"] = SPUtil.get("user_title", "") as String
        map["_id"] = SPUtil.get("user_id", "") as String
        map["verified"] = SPUtil.get("user_verified", false) as Boolean
        map
    }
}