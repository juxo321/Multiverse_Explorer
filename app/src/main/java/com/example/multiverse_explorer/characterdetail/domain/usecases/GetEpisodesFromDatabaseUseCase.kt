package com.example.multiverse_explorer.characterdetail.domain.usecases

import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characterdetail.domain.repository.CharacterDetailRepository
import com.example.multiverse_explorer.core.ResultApi
import javax.inject.Inject

class GetEpisodesFromDatabaseUseCase @Inject constructor(private val characterDetailRepository: CharacterDetailRepository) {

    suspend operator fun invoke(episodeIds: List<Int>): ResultApi<List<EpisodeDomain>> {
        return characterDetailRepository.getEpisodesFromDatabase(episodeIds = episodeIds)
    }
}