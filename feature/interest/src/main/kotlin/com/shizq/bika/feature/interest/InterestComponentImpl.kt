package com.shizq.bika.feature.interest

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.NetworkCategories
import com.shizq.bika.core.result.Result
import com.shizq.bika.core.result.asResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class InterestComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
) : InterestComponent, ComponentContext by componentContext {
    override val interestUiState = flow { emit(network.getCategories()) }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> InterestsUiState.Empty
                Result.Loading -> InterestsUiState.Loading
                is Result.Success -> InterestsUiState.Interests(result.data.mapToInterests())
            }
        }.stateIn(
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

private fun NetworkCategories.mapToInterests() = categories.map {
    Interest(
        it.id,
        it.isWeb,
        it.link,
        it.title,
        it.thumb.imageUrl
    )
}
