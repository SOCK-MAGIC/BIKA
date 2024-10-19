package com.shizq.bika.core.datastore

import androidx.datastore.core.DataStore
import com.shizq.bika.core.datastore.model.UserInterests
import javax.inject.Inject

class BikaInterestsDataSource @Inject constructor(
    private val userInterests: DataStore<UserInterests>,
) {
    val userData = userInterests.data

    suspend fun setInterestVisibility(title: String, state: Boolean) {
        userInterests.updateData {
            val newMap = it.interestsVisibility.toMutableMap()
            newMap[title] = state
            it.copy(interestsVisibility = newMap)
        }
    }
}
