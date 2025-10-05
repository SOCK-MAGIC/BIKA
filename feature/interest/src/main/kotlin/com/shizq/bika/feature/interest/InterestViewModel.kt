package com.shizq.bika.feature.interest

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.NetworkCategories
import com.shizq.bika.core.result.Result
import com.shizq.bika.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestViewModel @Inject constructor(
    private val network: BikaNetworkDataSource,
    userCredential: BikaUserCredentialDataSource,
    private val preferencesDataSource: BikaPreferencesDataSource,
) : ViewModel() {
    val state: LazyGridState = LazyGridState()
    val topicsUiState =
        preferencesDataSource.userData.map {
            TopicsUiState.Success(it.topics)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            TopicsUiState.Loading,
        )

    val interestUiState = combine(
        flow { emit(network.getCategories()) }.asResult(),
        userCredential.userData,
        preferencesDataSource.userData,
    ) { result, credential, preferences ->
        when (result) {
            is Result.Error -> InterestsUiState.Empty
            Result.Loading -> InterestsUiState.Loading
            is Result.Success -> {
                val interests =
                    FixedInterests + result.data.categories.map { it.asInterest(credential.token) }
                InterestsUiState.Interests(
                    interests.filter {
                        preferences.topics.getOrElse(it.title) { true }
                    },
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = InterestsUiState.Loading,
    )

    fun updateTopicSelection(title: String, state: Boolean) {
        viewModelScope.launch {
            preferencesDataSource.setTopicIdFollowed(title, state)
        }
    }
}

private fun NetworkCategories.Category.asInterest(token: String): Interest {
    val newLink =
        if (!isWeb) link else "$link/?token=$token&secret=pb6XkQ94iBBny1WUAxY0dY5fksexw0dt"
    return Interest(
        title = title,
        model = "https://s3.picacomic.com/static/${thumb.path}",
        id = id,
        isWeb = isWeb,
        link = newLink,
    )
}

private val FixedInterests = listOf(
    Interest("推荐", R.drawable.feature_interest_bika),
    Interest("排行榜", R.drawable.feature_interest_cat_leaderboard),
    Interest("游戏推荐", R.drawable.feature_interest_cat_game),
    Interest("哔咔小程序", R.drawable.feature_interest_cat_love_pica),
    Interest("留言板", R.drawable.feature_interest_cat_forum),
    Interest("最近更新", R.drawable.feature_interest_cat_latest),
    Interest("随机本子", R.drawable.feature_interest_cat_random),
)
