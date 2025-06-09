package com.example.multiverse_explorer.characters.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.characters.domain.usecases.GetCharactersFromNetworkUseCase
import com.example.multiverse_explorer.characters.domain.usecases.GetCharactersUseCase
import com.example.multiverse_explorer.characters.domain.usecases.UpdateFavoriteCharacterUseCase
import com.example.multiverse_explorer.core.Constants.Filter.ALL
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.domain.status.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersFromNetworkUseCase: GetCharactersFromNetworkUseCase,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val updateFavoriteCharacterUseCase: UpdateFavoriteCharacterUseCase,
) : ViewModel() {


    private val _characters: MutableStateFlow<List<CharacterDomain>> = MutableStateFlow(emptyList())
    val characters: StateFlow<List<CharacterDomain>> = _characters

    private val _selectedStatus = MutableStateFlow(ALL)
    val selectedStatus: StateFlow<String> = _selectedStatus

    var isNameSorted = mutableStateOf(false)
        private set

    var charactersUiState: UiState by mutableStateOf(UiState.Loading)
        private set

    private var databaseJob: Job? = null


    init {
        charactersUiState = UiState.Loading
        getCharactersFromDatabase()
        getCharactersFromNetwork(selectedStatus = (if (_selectedStatus.value == ALL) "" else _selectedStatus.value))
    }

    fun onStatusSelected(status: String) {
        _selectedStatus.value = status
        charactersUiState = UiState.Loading
        getCharactersFromNetwork((if (status == ALL) "" else status))
        getCharactersFromDatabase()
    }

    private fun getCharactersFromDatabase() {
        databaseJob?.cancel()
        databaseJob = viewModelScope.launch(Dispatchers.IO) {
            _selectedStatus
                .flatMapLatest { status ->
                    val newSelectedStatus = if (status == ALL) "" else status
                    getCharactersUseCase(selectedStatus = newSelectedStatus)
                }
                .collect { result ->
                    when (result) {
                        is ResultApi.Success -> {
                            if (result.data.isEmpty()) {
                                charactersUiState = UiState.Error("There are no results!")
                            } else {
                                _characters.value = if (isNameSorted.value) {
                                    result.data.sortedBy { it.name }
                                } else {
                                    result.data
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
        viewModelScope.launch(Dispatchers.IO) {
            characters.value.forEach {
                if (it.id == characterId) {
                    updateFavoriteCharacterUseCase(
                        characterId = characterId,
                        isFavorite = !it.favorite
                    )
                }
            }
        }
    }


    fun onSortByNameToggled() {
        isNameSorted.value = !isNameSorted.value
        if (isNameSorted.value) {
            _characters.value = _characters.value.sortedBy { it.name }
        } else {
            onStatusSelected(selectedStatus.value)
        }
    }


}