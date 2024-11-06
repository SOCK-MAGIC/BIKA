package com.shizq.bika.feature.interest

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Interest(
    val title: String,
    val model: Any,
    val id: String = Uuid.random().toString(),
    val isWeb: Boolean = false,
    val link: String = "",
)
