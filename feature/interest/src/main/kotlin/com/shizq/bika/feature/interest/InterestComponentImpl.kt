package com.shizq.bika.feature.interest

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
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
import kotlinx.coroutines.flow.stateIn

class InterestComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
    dataStore: BikaUserCredentialDataSource,
) : InterestComponent,
    ComponentContext by componentContext {

    override val interestUiState = flow { emit(network.getCategories()) }
        .asResult()
        .combine(dataStore.userData) { result, data ->
            when (result) {
                is Result.Error -> InterestsUiState.Empty
                Result.Loading -> InterestsUiState.Loading
                is Result.Success -> InterestsUiState.Interests(result.data.mapToInterests(data.token))
            }
        }
        .stateIn(
            scope = componentScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InterestsUiState.Loading,
        )

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

private fun NetworkCategories.mapToInterests(token: String) = categories.map {
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
