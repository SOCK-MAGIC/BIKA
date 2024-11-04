package com.shizq.bika.core.network.data

import com.shizq.bika.core.network.di.NetworkJson
import com.shizq.bika.core.network.model.Box
import com.shizq.bika.core.network.model.ResponseMessage
import io.ktor.client.statement.HttpResponse
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import javax.inject.Inject

class HttpResponseMonitor @Inject constructor() {
    val responseState: Channel<ResponseMessage> = Channel { }

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun transform(
        response: HttpResponse,
        content: ByteReadChannel,
        requestedType: TypeInfo,
    ): Any? {
        val box = NetworkJson.decodeFromStream(
            Box.serializer(serializer(requestedType.kotlinType!!)),
            content.toInputStream(),
        )
        responseState.send(ResponseMessage(box.code, box.message))
        return box.data
    }
}
