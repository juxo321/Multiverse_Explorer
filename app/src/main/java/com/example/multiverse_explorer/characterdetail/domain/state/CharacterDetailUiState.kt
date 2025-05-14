package com.example.multiverse_explorer.characterdetail.domain.state


sealed class CharacterDetailUiState {
    data object Loading : CharacterDetailUiState()
    data object Success : CharacterDetailUiState()
    data class Error(val message: String) : CharacterDetailUiState()
}