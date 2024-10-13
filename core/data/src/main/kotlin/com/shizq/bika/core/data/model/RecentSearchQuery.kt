package com.shizq.bika.core.data.model

data class RecentSearchQuery(
    val query: String,
    // val queriedDate: Instant = Clock.System.now(),
)
// fun RecentSearchQueryEntity.asExternalModel() = RecentSearchQuery(
//     query = query,
//     queriedDate = queriedDate,
// )
