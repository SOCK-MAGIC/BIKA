package com.shizq.bika.feature.interest

sealed interface TopicsUiState {
    data object Loading : TopicsUiState
    data class Success(val topics: Map<String, Boolean>) : TopicsUiState
}
