package com.shizq.bika.core.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Box<T>(
    val code: Int = 0,
    val message: String? = null,
    val error: String? = null,
    val data: T? = null,
)
