package com.shizq.bika.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoriesBean(
    val categories: List<Category>
) {
    @Serializable
    data class Category(
        @SerialName("_id")
        val _id: String,
        val active: Boolean,
        val description: String,
        val isWeb: Boolean,
        val link: String,
        val thumb: Thumb,
        var title: String,
        var imageRes: Int?
    ) {
        @Serializable
        data class Thumb(
            val fileServer: String,
            val originalName: String,
            val path: String
        )
    }
}