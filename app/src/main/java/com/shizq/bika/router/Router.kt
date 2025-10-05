package com.shizq.bika.router

import com.shizq.bika.core.navigation.BikaNavKey
import kotlinx.serialization.Serializable

@Serializable
data object InterestsRoute : BikaNavKey {
    override val isTopLevel: Boolean
        get() = true
}