package com.shizq.bika.core.model.data

/**
 * Class summarizing user interest data
 */
data class UserData(
    val topics: Map<String, Boolean>,
    val hobbies: Map<String, Boolean>,
    val badHobbies: Set<String>,
)
