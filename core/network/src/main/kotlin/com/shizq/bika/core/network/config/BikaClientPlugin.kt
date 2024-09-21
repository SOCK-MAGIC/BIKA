package com.shizq.bika.core.network.config

import com.shizq.bika.core.network.model.Box
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer

@OptIn(ExperimentalSerializationApi::class)
internal val BikaClientPlugin = createClientPlugin("unwrapResponse", ::ClientConfig) {
    val json = pluginConfig.transform
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

internal class ClientConfig {
    var transform: Json = Json
}
