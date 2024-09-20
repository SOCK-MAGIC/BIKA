package com.shizq.bika.core.network.ktor

import com.shizq.bika.core.network.model.Box
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer

internal class ClientConfig {
    var json: Json = Json
}

@OptIn(ExperimentalSerializationApi::class)
internal fun bikaClientPlugin() {
    createClientPlugin("bikaClientPlugin", ::ClientConfig) {
        val json = pluginConfig.json
        onRequest { request, content ->

 request.method.toString()       }
        transformResponseBody { response, content, requestedType ->
            val box = json.decodeFromStream(
                Box.serializer(serializer(requestedType.kotlinType!!)),
                content.toInputStream(),
            )
            if (box.code != 200) {
                throw Exception(box.message)
            }
            box.data
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
internal val unwrapResponse = { json: Json ->
    createClientPlugin("unwrapResponse") {
        transformResponseBody { response, content, requestedType ->
            val box = json.decodeFromStream(
                Box.serializer(serializer(requestedType.kotlinType!!)),
                content.toInputStream(),
            )
            if (box.code != 200) {
                throw Exception(box.message)
            }
            box.data
        }
    }
}
