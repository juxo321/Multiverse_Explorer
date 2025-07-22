package com.example.multiverse_explorer.characterdetail.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetCharacterDetailFromDatabaseUseCase
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetCharacterDetailFromNetworkUseCase
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetEpisodesFromDatabaseUseCase
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetEpisodesFromNetworkUseCase
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetIdsFromEpisodesUseCase
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.domain.status.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterDetailFromDatabaseUseCase: GetCharacterDetailFromDatabaseUseCase,
    private val getCharacterDetailFromNetworkUseCase: GetCharacterDetailFromNetworkUseCase,
    private val getEpisodesFromDatabaseUseCase: GetEpisodesFromDatabaseUseCase,
    private val getEpisodesFromNetworkUseCase: GetEpisodesFromNetworkUseCase,
    private val getIdsFromEpisodesUseCase: GetIdsFromEpisodesUseCase
) : ViewModel() {

    private val _characterDetail: MutableStateFlow<CharacterDetailDomain?> = MutableStateFlow(null)
    val characterDetail: StateFlow<CharacterDetailDomain?> = _characterDetail

    private val _episodes: MutableStateFlow<List<EpisodeDomain>> = MutableStateFlow(emptyList())
    val episodes: StateFlow<List<EpisodeDomain>> = _episodes

    var characterDetailUiState: UiState by mutableStateOf(UiState.Loading)
        private set

    fun getCharacterDetail(characterId: Int) {
        viewModelScope.launch {
            val result = getCharacterDetailFromDatabaseUseCase(characterId = characterId)
            when (result) {
                is ResultApi.Success -> {
                    _characterDetail.value = result.data
                    getEpisodes(result.data.episodes)
                }

                is ResultApi.Error -> {
                    getCharacterDetailFromNetwork(characterId = characterId)
                }
            }
        }
    }

    private suspend fun getCharacterDetailFromNetwork(characterId: Int) {
        val result = getCharacterDetailFromNetworkUseCase(characterId)
        when (result) {
            is ResultApi.Success -> {
                if (result.data == null) {
                    characterDetailUiState = UiState.Error("Character not found!")
                } else {
                    _characterDetail.value = result.data
                    getEpisodes(result.data.episodes)
                }
            }

            is ResultApi.Error -> {
                characterDetailUiState = UiState.Error(result.message)
            }

        }
    }

    fun getEpisodes(episodes: List<String>) {
        viewModelScope.launch {
            val episodeIds = getIdsFromEpisodesUseCase(episodes = episodes)
            val result = getEpisodesFromDatabaseUseCase(episodeIds = episodeIds)
            when (result) {
                is ResultApi.Success -> {
                    _episodes.value = result.data
                    characterDetailUiState = UiState.Success
                }

                is ResultApi.Error -> {
                    getEpisodesFromNetwork(episodeIds)
                }

            }
        }
    }

    private suspend fun getEpisodesFromNetwork(episodeIds: List<Int>) {
        val result = getEpisodesFromNetworkUseCase(episodeIds = episodeIds)
        when (result) {
            is ResultApi.Success -> {
                if (result.data?.isEmpty() == true) {
                    characterDetailUiState = UiState.Error("Episodes not found!")
                } else {
                    _episodes.value = result.data ?: emptyList()
                    characterDetailUiState = UiState.Success
                }
            }

            is ResultApi.Error -> {
                characterDetailUiState = UiState.Error(result.message)
            }

        }
    }


}
