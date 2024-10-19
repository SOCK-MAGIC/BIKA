package com.shizq.bika.feature.interest

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.datastore.BikaInterestsDataSource
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

class InterestComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
    private val userInterests: BikaInterestsDataSource,
    userCredential: BikaUserCredentialDataSource,
) : InterestComponent,
    ComponentContext by componentContext {
    override val interestVisibilityUiState = userInterests.userData.map {
        it.interestsVisibility
    }
        .stateIn(
            componentScope,
            SharingStarted.WhileSubscribed(5000),
            mutableMapOf(),
        )
    override val interestUiState = combine(
        flow { emit(network.getCategories()) }.asResult(),
        userCredential.userData,
        userInterests.userData,
    ) { result, credential, interest ->
        when (result) {
            is Result.Error -> InterestsUiState.Empty
            Result.Loading -> InterestsUiState.Loading
            is Result.Success -> {
                val interests =
                    result.data.categories
                        .filter { interest.interestsVisibility.getOrElse(it.title) { true } }
                        .map { it.asInterest(credential.token) }
                InterestsUiState.Interests(interests)
            }
        }
    }
        .stateIn(
            scope = componentScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InterestsUiState.Loading,
        )

    override fun updateInterestVisibility(title: String, state: Boolean) {
        componentScope.launch {
            userInterests.setInterestVisibility(title, state)
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

data class Interest(
    val id: String,
    val isWeb: Boolean,
    val link: String,
    val title: String,
    val imageUrl: String,
)

private fun NetworkCategories.Category.asInterest(token: String): Interest {
    val newLink =
        if (isWeb) "$link/?token=$token&secret=pb6XkQ94iBBny1WUAxY0dY5fksexw0dt" else link
    return Interest(
        id,
        isWeb,
        newLink,
        title,
        if (title == "嗶咔小禮物" || title == "嗶咔畫廊") {
            "https://s3.picacomic.com/static/${thumb.path}"
        } else {
            thumb.imageUrl
        }
    )
}

private fun NetworkCategories.mapToInterests(token: String) =
    categories.map {
        val newLink =
            if (it.isWeb) "${it.link}/?token=$token&secret=pb6XkQ94iBBny1WUAxY0dY5fksexw0dt" else it.link

        if (!arrayOf("嗶咔小禮物", "嗶咔畫廊").contains(it.title)) {
            Interest(
                it.id,
                it.isWeb,
                newLink,
                it.title,
                it.thumb.imageUrl,
            )
        } else {
            Interest(
                it.id,
                it.isWeb,
                newLink,
                it.title,
                "https://s3.picacomic.com/static/${it.thumb.path}",
            )
        }
    }
