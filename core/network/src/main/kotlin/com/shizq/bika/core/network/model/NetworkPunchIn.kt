package com.shizq.bika.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkPunchIn(
    val res: Res = Res(),
) {
    @Serializable
    data class Res(
        val punchInLastDay: String = "",
        val status: String = "",
    )
}
