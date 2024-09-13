package com.shizq.bika.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val addresses: List<String> = emptyList()
)
