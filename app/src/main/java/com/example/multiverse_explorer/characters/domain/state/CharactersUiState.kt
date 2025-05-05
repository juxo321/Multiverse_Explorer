package com.example.multiverse_explorer.characters.domain.state


sealed class CharactersUiState {
    data object Loading: CharactersUiState()
    data object Success: CharactersUiState()
    data class Error(val message: String): CharactersUiState()

}