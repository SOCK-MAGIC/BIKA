package com.shizq.bika.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserCredential(
    val email: String = "",
    val password: String = "",
    val token: String = "",
)
