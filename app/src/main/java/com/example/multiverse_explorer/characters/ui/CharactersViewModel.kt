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
): ViewModel() {


    private val _characters: MutableStateFlow<List<CharacterDomain>> = MutableStateFlow(emptyList())
    val characters: StateFlow<List<CharacterDomain>> = _characters

    var charactersUiState: CharactersUiState by mutableStateOf(CharactersUiState.Loading)
        private set


    init {
        getCharacters()
    }


    private fun getCharacters(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCharactersUseCase()
            when(result){
                is ResultApi.Success -> {
                    if (result.data.isEmpty()){
                            charactersUiState = CharactersUiState.Error("There are no results!")
                    }else{
                        _characters.value = result.data
                        charactersUiState = CharactersUiState.Success
                    }
                }
                is ResultApi.Error -> charactersUiState = CharactersUiState.Error(result.message)

            }
        }
    }
}