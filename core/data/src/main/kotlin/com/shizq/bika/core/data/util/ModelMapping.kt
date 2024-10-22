package com.shizq.bika.core.data.util

import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.model.ComicInSearch
import com.shizq.bika.core.network.model.NetworkComicList
import com.shizq.bika.core.network.model.NetworkComicSimple

fun ComicInSearch.Comics.Doc.asComicResource() =
    ComicResource(
        id,
        thumb.imageUrl,
        title,
        author,
        categories,
        finished,
        0,
        0,
        likesCount,
    )

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

fun NetworkComicSimple.asComicResource(): ComicResource = ComicResource(
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
