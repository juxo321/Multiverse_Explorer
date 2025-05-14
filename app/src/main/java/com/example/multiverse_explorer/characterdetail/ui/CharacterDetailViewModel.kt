package com.example.multiverse_explorer.characterdetail.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characterdetail.domain.state.CharacterDetailUiState
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetCharacterDetailUseCase
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetEpisodesByIdsUseCase
import com.example.multiverse_explorer.core.ResultApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase,
    private val getEpisodesByIdsUseCase: GetEpisodesByIdsUseCase
): ViewModel() {

    private val _characterDetail: MutableStateFlow<CharacterDetailDomain?> = MutableStateFlow(null)
    val characterDetail: StateFlow<CharacterDetailDomain?> = _characterDetail

    private val _episodes: MutableStateFlow<List<EpisodeDomain>> = MutableStateFlow(emptyList())
    val episodes: StateFlow<List<EpisodeDomain>> = _episodes

    var characterDetailUiState: CharacterDetailUiState by mutableStateOf(CharacterDetailUiState.Loading)
        private set

    fun getCharacterDetail(characterId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCharacterDetailUseCase(characterId)
            when(result){
                is ResultApi.Success -> {
                    if (result.data == null) {
                        characterDetailUiState = CharacterDetailUiState.Error("Character not found!")
                    } else {
                        _characterDetail.value = result.data
                        getEpisodes(result.data.episodes)
                    }
                }
                is ResultApi.Error -> {
                    characterDetailUiState = CharacterDetailUiState.Error(result.message)
                }

            }
        }
    }

    fun getEpisodes(episodes: List<String>){
        viewModelScope.launch(Dispatchers.IO) {
            val result = getEpisodesByIdsUseCase(episodes = episodes)
            when(result){
                is ResultApi.Success -> {
                    if (result.data?.isEmpty() == true) {
                        characterDetailUiState = CharacterDetailUiState.Error("Episodes not found!")
                    } else {
                        _episodes.value = result.data ?: emptyList()
                        characterDetailUiState = CharacterDetailUiState.Success
                    }
                }
                is ResultApi.Error -> {
                    characterDetailUiState = CharacterDetailUiState.Error(result.message)
                }

            }
        }
    }




}
