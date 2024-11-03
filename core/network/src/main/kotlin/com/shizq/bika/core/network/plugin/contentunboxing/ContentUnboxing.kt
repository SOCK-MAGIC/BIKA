package com.shizq.bika.core.network.plugin.contentunboxing

import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.util.AttributeKey
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel

public val SkipContentUnboxing = AttributeKey<Boolean>("skip-content-unboxing")

internal val ContentUnboxing: ClientPlugin<ContentUnboxingConfig> = createClientPlugin(
    "ContentUnboxing",
    ::ContentUnboxingConfig,
) {
    val block = pluginConfig.block
    transformResponseBody { response, content, requestedType ->
        if (response.request.attributes.getOrNull(SkipContentUnboxing) == true) {
            return@transformResponseBody null
        }
        block(response, content, requestedType)
    }
}

internal class ContentUnboxingConfig {
    private var _block: suspend (HttpResponse, ByteReadChannel, TypeInfo) -> Any? = { _, _, _ -> null }
    public var block: suspend (HttpResponse, ByteReadChannel, TypeInfo) -> Any?
        get() = _block
        set(value) {
            _block = value
        }

    fun transform(block: suspend (response: HttpResponse, content: ByteReadChannel, requestedType: TypeInfo) -> Any?) {
        _block = block
    }
}
