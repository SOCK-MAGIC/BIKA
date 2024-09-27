package com.shizq.bika.core.network.config

import com.shizq.bika.core.datastore.model.Account
import com.shizq.bika.core.network.model.Box
import com.shizq.bika.core.network.model.NetworkToken
import io.ktor.client.call.body
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer

@OptIn(ExperimentalSerializationApi::class)
internal val BikaClientPlugin = createClientPlugin("BikaPlugin", ::ClientConfig) {
    val json = pluginConfig.transform
    val account = pluginConfig.account
    val save = pluginConfig.save
    suspend fun refreshToken(email: String, password: String) {
        val token = client.post("auth/sign-in") {
            setBody("""{"email":"$email","password":"$password"}""")
        }.body<NetworkToken>()
        save(token.token)
    }
    transformResponseBody { response, content, requestedType ->
        val box = json.decodeFromStream(
            Box.serializer(serializer(requestedType.kotlinType!!)),
            content.toInputStream(),
        )
        if (box.code != 200) {
            if (box.code == 401 && box.error == "1005") {
                if (account != null) {
                    refreshToken(account.email, account.password)
                }
            }
            throw Exception(box.message)
        }
        box.data
    }

}

internal class ClientConfig {
    var transform: Json = Json
    var account: Account? = null
    var save: suspend (String) -> Unit = { _ -> }
}
