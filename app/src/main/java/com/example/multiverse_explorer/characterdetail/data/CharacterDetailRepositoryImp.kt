package com.example.multiverse_explorer.characterdetail.data

import com.example.multiverse_explorer.characterdetail.data.mappers.toDomain
import com.example.multiverse_explorer.characterdetail.data.model.CharacterDetailData
import com.example.multiverse_explorer.characterdetail.data.network.CharacterDetailService
import com.example.multiverse_explorer.characterdetail.data.network.EpisodeService
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characterdetail.domain.repository.CharacterDetailRepository
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import javax.inject.Inject

class CharacterDetailRepositoryImp @Inject constructor(
    private val characterDetailService: CharacterDetailService,
    private val episodeService: EpisodeService
) : CharacterDetailRepository {

    override suspend fun getCharacterDetail(characterId: Int): ResultApi<CharacterDetailDomain?> =
        NetworkFunctions.safeServiceCall(
            serviceCall = { characterDetailService.getCharacterDetail(characterId = characterId) },
            transform = { it?.toDomain() })

    override suspend fun getEpisodes(episodeIds: List<Int>): ResultApi<List<EpisodeDomain>?> =
        NetworkFunctions.safeServiceCall(
            serviceCall = { episodeService.getEpisodes(episodeIds = episodeIds) },
            transform = { it.map { episode -> episode.toDomain() } })

}