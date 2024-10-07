package com.shizq.bika.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val dns: Set<String> = setOf(),
    @Deprecated("UserCredential")
    val token: String = "",
    @Deprecated("UserCredential")
    val account: Account? = null
)

@Deprecated("UserCredential")
@Serializable
data class Account(
    val email: String,
    val password: String,
)

