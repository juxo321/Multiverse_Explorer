package com.example.multiverse_explorer.characters.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.characters.domain.state.CharactersUiState
import com.example.multiverse_explorer.characters.domain.usecases.GetCharactersUseCase
import com.example.multiverse_explorer.core.ResultApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {


    private val _characters: MutableStateFlow<List<CharacterDomain>> = MutableStateFlow(emptyList())
    val characters: StateFlow<List<CharacterDomain>> = _characters

    var selectedStatus = mutableStateOf("All")
        private set

    var charactersUiState: CharactersUiState by mutableStateOf(CharactersUiState.Loading)
        private set

    private val favoriteCharacters: MutableList<Int> = mutableListOf()


    init {
        onStatusSelected(status = selectedStatus.value)
    }

    fun onStatusSelected(status: String) {
        selectedStatus.value = status
        charactersUiState = CharactersUiState.Loading
        getCharacters((if (status == "All") "" else status))
    }


    private fun getCharacters(selectedStatus: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCharactersUseCase(selectedStatus = selectedStatus)
            when (result) {
                is ResultApi.Success -> {
                    if (result.data.isEmpty()) {
                        charactersUiState = CharactersUiState.Error("There are no results!")
                    } else {
                        _characters.value = result.data.map { character ->
                            character.copy(favorite = favoriteCharacters.contains(character.id))
                        }
                        charactersUiState = CharactersUiState.Success
                    }
                }

                is ResultApi.Error -> charactersUiState = CharactersUiState.Error(result.message)

            }
        }
    }

    fun toggleFavorite(characterId: Int) {
        _characters.value = characters.value.map {
            if (it.id == characterId) {
                it.copy(favorite = !it.favorite).also { newCharacter ->
                    updateFavoritesList(
                        characterId = newCharacter.id,
                        isFavorite = newCharacter.favorite
                    )
                }
            } else {
                it
            }
        }
    }


    private fun updateFavoritesList(characterId: Int, isFavorite: Boolean) {
        if (isFavorite && !favoriteCharacters.contains(characterId)) {
            favoriteCharacters.add(characterId)
        } else if (!isFavorite && favoriteCharacters.contains(characterId)) {
            favoriteCharacters.remove(characterId)
        }
    }

    fun onSortByNameToggled(enabled: Boolean) {
        if (enabled) {
            _characters.value = _characters.value.sortedBy { it.name }
        } else {
            onStatusSelected(selectedStatus.value)
        }
    }


}