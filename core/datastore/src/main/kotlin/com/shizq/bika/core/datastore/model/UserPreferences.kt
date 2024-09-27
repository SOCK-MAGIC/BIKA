package com.shizq.bika.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val dns: Set<String> = setOf(),
    val token: String = "",
    val account: Account? = null
)

@Serializable
data class Account(
    val email: String = "",
    val password: String = "",
)
