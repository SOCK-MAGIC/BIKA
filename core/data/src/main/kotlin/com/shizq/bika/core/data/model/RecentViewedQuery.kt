package com.shizq.bika.core.data.model

import com.shizq.bika.core.database.model.RecentWatchedComicQueryEntity
import com.shizq.bika.core.model.ComicResource

data class RecentViewedQuery(
    val id: String,
)

fun RecentWatchedComicQueryEntity.asExternalModel() = ComicResource(
    id = id,
    imageUrl = imageUrl,
    title = title,
    author = author,
    categories = categories.split(","),
    finished = finished,
    epsCount = epsCount,
    pagesCount = pagesCount,
    likeCount = likeCount,
)
