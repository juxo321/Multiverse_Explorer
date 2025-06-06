package com.example.multiverse_explorer.characterdetail.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characterdetail.domain.state.CharacterDetailUiState
import com.example.multiverse_explorer.characterdetail.ui.states.CharacterDetailSuccessState
import com.example.multiverse_explorer.core.ui.components.ErrorState
import com.example.multiverse_explorer.core.ui.components.LoadingState


@Composable
fun CharacterDetailScreen(
    characterDetailViewModel: CharacterDetailViewModel = hiltViewModel(),
    characterId: Int,
    modifier: Modifier
) {
    LaunchedEffect(true) {
        characterDetailViewModel.getCharacterDetail(characterId)
    }

    val characterDetail: CharacterDetailDomain? by characterDetailViewModel.characterDetail.collectAsState(
        null
    )
    val episodes: List<EpisodeDomain> by characterDetailViewModel.episodes.collectAsState()
    val characterDetailUiState: CharacterDetailUiState =
        characterDetailViewModel.characterDetailUiState


    when (characterDetailUiState) {
        CharacterDetailUiState.Loading -> {
            LoadingState(modifier = modifier)
        }

        CharacterDetailUiState.Success -> {
            characterDetail?.let {
                CharacterDetailSuccessState(
                    characterDetail = it,
                    episodes = episodes,
                    modifier = modifier
                )
            }
        }

        is CharacterDetailUiState.Error -> {
            ErrorState(modifier = modifier)
        }
    }
}