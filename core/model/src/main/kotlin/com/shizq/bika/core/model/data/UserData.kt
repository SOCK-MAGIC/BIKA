package com.shizq.bika.core.model.data

/**
 * Class summarizing user interest data
 */
data class UserData(
    val subscribeInterests: Map<String, Boolean>,
    val subscribeCategories: Map<String, Boolean>,
    val unsubscribeInterested: Map<String, Boolean>,
)
