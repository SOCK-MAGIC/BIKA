package com.shizq.bika.core.data.model

import com.shizq.bika.core.database.model.RecentViewedQueryEntity

data class RecentViewedQuery(
    val id: String,
)

fun RecentViewedQueryEntity.asExternalModel() = RecentViewedQuery(id = id)
