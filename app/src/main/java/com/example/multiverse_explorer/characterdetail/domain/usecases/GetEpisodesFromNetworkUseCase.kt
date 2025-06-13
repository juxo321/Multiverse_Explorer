package com.example.multiverse_explorer.characterdetail.domain.usecases

import com.example.multiverse_explorer.characterdetail.domain.repository.CharacterDetailRepository
import javax.inject.Inject

class GetEpisodesFromNetworkUseCase @Inject constructor(private val characterDetailRepository: CharacterDetailRepository) {

    suspend operator fun invoke(episodeIds: List<Int>) =
        characterDetailRepository.getEpisodesFromNetwork(episodeIds = episodeIds)
}