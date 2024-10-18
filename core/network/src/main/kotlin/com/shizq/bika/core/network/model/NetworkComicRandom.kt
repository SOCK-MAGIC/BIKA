package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class NetworkComicRandom(
    @SerialName("comics")
    val comics: List<NetworkComicSimple> = listOf(),
)
