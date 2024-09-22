package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkInit(
    @SerialName("addresses")
    val addresses: Set<String> = setOf("172.67.194.19", "104.21.20.188"),
    @SerialName("status")
    val status: String = ""
)
