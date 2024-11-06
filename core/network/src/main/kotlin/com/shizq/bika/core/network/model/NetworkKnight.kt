package com.shizq.bika.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkKnight(
    val networkUsers: List<NetworkUser>,
)
