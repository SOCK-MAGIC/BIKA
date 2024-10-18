package com.shizq.bika.feature.comic.model

import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.model.NetworkComicList

fun NetworkComicList.Comics.Doc.asComicResource() = ComicResource(
    id,
    thumb.imageUrl,
    title,
    author,
    categories,
    finished,
    epsCount,
    pagesCount,
    likesCount,
)

fun NetworkComicList.Comics.asComicResource() = docs.map { it.asComicResource() }

data class Comic(
    val id: String,
    val title: String,
    val finished: Boolean,
    val author: String,
    val likesCount: Int,
    val total: Int,
    val categories: String,
    val thumbUrl: String,
    val epsCount: Int,
    val pagesCount: Int,
)
