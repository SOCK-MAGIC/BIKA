package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Thumb(
    @SerialName("fileServer")
    val fileServer: String = "",
    @SerialName("originalName")
    val originalName: String = "",
    @SerialName("path")
    val path: String = ""
)
