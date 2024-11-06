package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkUserProfile(
    @SerialName("user")
    val netWorkUser: NetworkUser = NetworkUser(),
)

