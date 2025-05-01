package com.example.multiverse_explorer.characters.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.multiverse_explorer.characters.domain.state.CharactersUiState
import com.example.multiverse_explorer.characters.ui.states.CharactersErrorState
import com.example.multiverse_explorer.characters.ui.states.CharactersLoadingState
import com.example.multiverse_explorer.characters.ui.states.CharactersSuccessState


@Composable
fun CharactersScreen(
    charactersViewModel: CharactersViewModel = hiltViewModel(),
    modifier: Modifier
) {

    val charactersUiState = charactersViewModel.charactersUiState
    val characters by charactersViewModel.characters.collectAsState(emptyList())

    when (charactersUiState) {
        CharactersUiState.Loading -> CharactersLoadingState(modifier = modifier)
        CharactersUiState.Success -> CharactersSuccessState(
            modifier = modifier,
            characters = characters
        )
        is CharactersUiState.Error -> CharactersErrorState(modifier = modifier)
    }
}