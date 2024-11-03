package com.shizq.bika.core.network.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Docs<T : KSerializer<T>>(
    @SerialName("docs")
    val docs: List<T> = listOf(),
    @SerialName("limit")
    val limit: Int = 0,
    @SerialName("page")
    val page: String = "",
    @SerialName("pages")
    val pages: Int = 0,
    @SerialName("total")
    val total: Int = 0,
)
