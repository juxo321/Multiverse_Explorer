package com.example.multiverse_explorer.characterdetail.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characterdetail.ui.states.CharacterDetailSuccessState
import com.example.multiverse_explorer.core.domain.status.UiState
import com.example.multiverse_explorer.core.ui.components.ErrorState
import com.example.multiverse_explorer.core.ui.components.LoadingState


@Composable
fun CharacterDetailScreen(
    characterDetailViewModel: CharacterDetailViewModel = hiltViewModel(),
    characterId: Int,
    modifier: Modifier
) {
    LaunchedEffect(characterId) {
        characterDetailViewModel.getCharacterDetail(characterId)
    }

    val characterDetail: CharacterDetailDomain? by characterDetailViewModel.characterDetail.collectAsState(
        null
    )
    val episodes: List<EpisodeDomain> by characterDetailViewModel.episodes.collectAsState()
    val characterDetailUiState: UiState = characterDetailViewModel.characterDetailUiState


    when (characterDetailUiState) {
        UiState.Loading -> {
            LoadingState(modifier = modifier)
        }

        UiState.Success -> {
            characterDetail?.let {
                CharacterDetailSuccessState(
                    characterDetail = it,
                    episodes = episodes,
                    modifier = modifier
                )
            }
        }

        is UiState.Error -> {
            ErrorState(modifier = modifier)
        }
    }
}