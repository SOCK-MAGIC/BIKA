package com.shizq.bika.core.datastore

import androidx.datastore.core.DataStore
import com.shizq.bika.core.datastore.model.Orientation
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
            hobbies = preferences.hobbies,
            badHobbies = preferences.topics.filterNot { it.value }.keys,
        )
    }

    suspend fun setHobbiesFollowed(name: String, state: Boolean) {
        userPreferences.updateData {
            it.copy(
                hobbies = it.hobbies.toMutableMap().apply {
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

    suspend fun setOrientation(orientation: Orientation) {
        userPreferences.updateData {
            it.copy(
                orientation = orientation,
            )
        }
    }
}
