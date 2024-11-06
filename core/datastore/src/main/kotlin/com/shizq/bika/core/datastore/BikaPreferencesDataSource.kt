package com.shizq.bika.core.datastore

import androidx.datastore.core.DataStore
import com.shizq.bika.core.datastore.model.UserPreferences
import com.shizq.bika.core.model.data.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BikaPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
    val userData2 = userPreferences.data.map { preferences ->
        UserData(
            subscribeInterests = preferences.interestsVisibilityState,
            subscribeCategories = preferences.categoriesVisibilityState,
            unsubscribeInterested = preferences.interestsVisibilityState.filterNot { it.value },
        )
    }

    suspend fun setCategoriesFollowed(name: String, state: Boolean) {
        userPreferences.updateData {
            it.copy(
                categoriesVisibilityState = it.categoriesVisibilityState.toMutableMap().apply {
                    this[name] = state
                },
            )
        }
    }

    suspend fun setInterestsFollowed(name: String, state: Boolean) {
        userPreferences.updateData {
            it.copy(
                interestsVisibilityState = it.interestsVisibilityState.toMutableMap().apply {
                    this[name] = state
                },
            )
        }
    }
}
