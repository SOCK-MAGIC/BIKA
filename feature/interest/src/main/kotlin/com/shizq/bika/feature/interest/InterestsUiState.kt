package com.shizq.bika.feature.interest

sealed interface InterestsUiState {
    data object Loading : InterestsUiState

    data class Interests(val interests: List<Interest>) : InterestsUiState

    data object Empty : InterestsUiState
}
