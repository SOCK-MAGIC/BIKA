package com.shizq.bika.core.datastore

import androidx.datastore.core.DataStore
import com.shizq.bika.core.datastore.model.UserPreferences
import com.shizq.bika.core.model.data.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BikaPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data.map { preferences ->
        UserData(
            topics = preferences.topics,
            subscribeCategories = preferences.categoriesVisibilityState,
            unsubscribedTopics = preferences.topics.filterNot { it.value },
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

    suspend fun setTopicIdFollowed(name: String, state: Boolean) {
        userPreferences.updateData {
            it.copy(
                topics = it.topics.toMutableMap().apply {
                    this[name] = state
                },
            )
        }
    }
}
