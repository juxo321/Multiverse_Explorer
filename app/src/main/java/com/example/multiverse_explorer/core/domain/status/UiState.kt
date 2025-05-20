package com.example.multiverse_explorer.core.domain.status

sealed class UiState {
    data object Loading : UiState()
    data object Success : UiState()
    data class Error(val message: String): UiState()

}