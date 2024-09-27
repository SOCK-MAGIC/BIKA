package com.shizq.bika.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCategories(
    val categories: List<Category> = emptyList()
) {
    @Serializable
    data class Category(
        @SerialName("_id")
        val id: String = "",
        val active: Boolean = false,
        val description: String = "",
        val isWeb: Boolean = false,
        val link: String = "",
        val thumb: Thumb = Thumb(),
        val title: String = "",
    ) {
        @Serializable
        data class Thumb(
            val fileServer: String = "",
            val originalName: String = "",
            val path: String = ""
        )
    }
}