package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkKnight(
    @SerialName("users")
    val networkUsers: List<NetworkUser>,
)
