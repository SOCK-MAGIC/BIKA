package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Thumb(
    @SerialName("fileServer")
    val fileServer: String = "",
    @SerialName("originalName")
    val originalName: String = "",
    @SerialName("path")
    val path: String = ""
) {
    @Transient
    val imageUrl = if (fileServer.endsWith("/")) {
        "$fileServer/$path"
    } else {
        "$fileServer/static/$path"
    }
}
