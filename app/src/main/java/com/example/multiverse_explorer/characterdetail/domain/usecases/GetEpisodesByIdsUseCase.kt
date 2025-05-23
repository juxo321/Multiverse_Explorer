package com.example.multiverse_explorer.characterdetail.domain.usecases

import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characterdetail.domain.repository.CharacterDetailRepository
import com.example.multiverse_explorer.core.ResultApi
import javax.inject.Inject

class GetEpisodesByIdsUseCase @Inject constructor(private val characterDetailRepository: CharacterDetailRepository) {

    suspend operator fun invoke(episodes: List<String>): ResultApi<List<EpisodeDomain>?> {

        val episodeIds = episodes.map {
            it.split("/").last().toInt()
        }

        return characterDetailRepository.getEpisodes(episodeIds)
    }
}