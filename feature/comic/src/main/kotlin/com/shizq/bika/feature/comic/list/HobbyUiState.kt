package com.shizq.bika.feature.comic.list

sealed interface HobbyUiState {
    data object Loading : HobbyUiState
    data class Success(val hobbies: Map<String, Boolean>) : HobbyUiState
}
