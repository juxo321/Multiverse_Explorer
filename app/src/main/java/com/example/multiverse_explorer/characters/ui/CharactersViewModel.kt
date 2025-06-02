package com.example.multiverse_explorer.characters.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.characters.domain.usecases.GetCharactersFromNetworkUseCase
import com.example.multiverse_explorer.characters.domain.usecases.GetCharactersUseCase
import com.example.multiverse_explorer.core.Constants.Filter.ALL
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.domain.status.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersFromNetworkUseCase: GetCharactersFromNetworkUseCase,
    private val getCharactersUseCase: GetCharactersUseCase,
) : ViewModel() {


    private val _characters: MutableStateFlow<List<CharacterDomain>> = MutableStateFlow(emptyList())
    val characters: StateFlow<List<CharacterDomain>> = _characters

    private val _selectedStatus = MutableStateFlow(ALL)
    val selectedStatus: StateFlow<String> = _selectedStatus

    var charactersUiState: UiState by mutableStateOf(UiState.Loading)
        private set

    private val favoriteCharacters: MutableList<Int> = mutableListOf()


    init {
        charactersUiState = UiState.Loading
        getCharactersFromDatabase()
        getCharactersFromNetwork(selectedStatus = selectedStatus.value)
    }

    fun onStatusSelected(status: String) {
        _selectedStatus.value = status
        charactersUiState = UiState.Loading
        getCharactersFromNetwork((if (status == ALL) "" else status))
    }

    private fun getCharactersFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedStatus
                .flatMapLatest {status ->
                    val newSelectedStatus = if (status == ALL) "" else status
                    getCharactersUseCase(selectedStatus = newSelectedStatus)
                }.distinctUntilChanged()
                .collect { result ->
                    Log.i("VALOR EMITIDO", result.toString()) //TODO investigate why is emitting multiple data
                    when (result) {
                        is ResultApi.Success -> {
                            if (result.data.isEmpty()) {
                                charactersUiState = UiState.Error("There are no results!")
                            } else {
                                _characters.value = result.data.map { character ->
                                    val isFavorite = favoriteCharacters.contains(character.id)
                                    character.copy(favorite = isFavorite)
                                }
                                withContext(Dispatchers.Main) {
                                    charactersUiState = UiState.Success
                                }
                            }
                        }

                        is ResultApi.Error -> {
                            charactersUiState = UiState.Error(result.message)
                        }
                    }
                }

        }
    }


    private fun getCharactersFromNetwork(selectedStatus: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getCharactersFromNetworkUseCase(selectedStatus = selectedStatus)
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