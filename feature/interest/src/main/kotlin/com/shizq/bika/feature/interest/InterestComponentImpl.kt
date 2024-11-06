package com.shizq.bika.feature.interest

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import com.shizq.bika.core.datastore.BikaUserCredentialDataSource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.NetworkCategories
import com.shizq.bika.core.result.Result
import com.shizq.bika.core.result.asResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class InterestComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
    userCredential: BikaUserCredentialDataSource,
    private val preferencesDataSource: BikaPreferencesDataSource,
) : InterestComponent,
    ComponentContext by componentContext {
    override val topicsUiState =
        preferencesDataSource.userData.map {
            TopicsUiState.Success(it.topics)
        }.stateIn(
            componentScope,
            SharingStarted.WhileSubscribed(5000),
            TopicsUiState.Loading,
        )
    override val interestUiState = combine(
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
    }
        .stateIn(
            scope = componentScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InterestsUiState.Loading,
        )

    override fun updateTopicSelection(title: String, state: Boolean) {
        componentScope.launch {
            preferencesDataSource.setTopicIdFollowed(title, state)
        }
    }

    @AssistedFactory
    interface Factory : InterestComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): InterestComponentImpl
    }
}

sealed interface InterestsUiState {
    data object Loading : InterestsUiState

    data class Interests(val interests: List<Interest>) : InterestsUiState

    data object Empty : InterestsUiState
}

@OptIn(ExperimentalUuidApi::class)
data class Interest(
    val title: String,
    val model: Any,
    val id: String = Uuid.random().toString(),
    val isWeb: Boolean = false,
    val link: String = "",
)

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
