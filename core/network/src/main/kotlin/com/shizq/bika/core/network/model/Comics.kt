package com.shizq.bika.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Comics(
    val category: String? = null,
    val tag: String? = null,
    val creatorId: String? = null,
    val chineseTeam: String? = null,
    val author: String? = null,
)
