package com.shizq.bika.core.network.model

enum class Sort(val value: String, val description: String) {
    SORT_DEFAULT("ua", ""),
    SORT_TIME_NEWEST("dd", "新到旧"),
    SORT_TIME_OLDEST("da", "旧到新"),
    SORT_LIKE_MOST("ld", "最多爱心"),
    SORT_VIVE_MOST("vd", "最多指数"),
}
