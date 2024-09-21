package com.shizq.bika.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val dns: Set<String> = setOf(),
    val token: String = "",
)
