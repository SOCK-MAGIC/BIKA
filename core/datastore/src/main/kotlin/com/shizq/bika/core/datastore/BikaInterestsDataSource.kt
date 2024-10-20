package com.shizq.bika.core.datastore

import androidx.datastore.core.DataStore
import com.shizq.bika.core.datastore.model.UserInterests
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BikaInterestsDataSource @Inject constructor(
    private val userInterests: DataStore<UserInterests>,
) {
    val userData = userInterests.data
    val userHideCategories =
        userInterests.data.map { interests -> interests.categoriesVisibility.filterValues { !it }.keys }

    suspend fun setInterestVisibility(title: String, state: Boolean) {
        userInterests.updateData {
            val newMap = it.interestsVisibility.toMutableMap()
            newMap[title] = state
            it.copy(interestsVisibility = newMap)
        }
    }

    suspend fun setCategoriesVisibility(category: String, state: Boolean) {
        userInterests.updateData {
            val newMap = it.categoriesVisibility.toMutableMap()
            newMap[category] = state
            it.copy(categoriesVisibility = newMap)
        }
    }
}
