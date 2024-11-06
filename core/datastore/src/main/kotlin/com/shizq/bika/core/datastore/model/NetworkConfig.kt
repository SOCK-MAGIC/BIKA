package com.shizq.bika.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkConfig(
    val dns: Set<String> = setOf(),
)
