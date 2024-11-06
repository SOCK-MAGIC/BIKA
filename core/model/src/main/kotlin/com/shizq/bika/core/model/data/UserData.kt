package com.shizq.bika.core.model.data

/**
 * Class summarizing user interest data
 */
data class UserData(
    val topics: Map<String, Boolean>,
    val subscribeCategories: Map<String, Boolean>,
    val unsubscribedTopics: Map<String, Boolean>,
)
