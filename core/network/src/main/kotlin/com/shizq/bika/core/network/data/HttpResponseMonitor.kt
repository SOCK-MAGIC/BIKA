package com.shizq.bika.core.network.data

import com.shizq.bika.core.network.model.ResponseMessage
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class HttpResponseMonitor @Inject constructor() {
    val responseState: Channel<ResponseMessage> = Channel { }

}
