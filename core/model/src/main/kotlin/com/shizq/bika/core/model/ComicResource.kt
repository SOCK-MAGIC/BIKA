package com.shizq.bika.core.model

data class ComicResource(
    val id: String,
    val imageUrl: String,
    val title: String,
    val author: String,
    val categories: String,
    val finished: Boolean,
    val epsCount: Int,
    val pagesCount: Int,
    val likeCount: Int,
)
