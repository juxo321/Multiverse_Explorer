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
import javax.inject.Inject

class CharacterDetailRepositoryImp @Inject constructor(
    private val characterDetailService: CharacterDetailService,
    private val episodeService: EpisodeService
) : CharacterDetailRepository {

    override suspend fun getCharacterDetail(characterId: Int): ResultApi<CharacterDetailDomain?> {
        val result = characterDetailService.getCharacterDetail(characterId = characterId)
        return when(result){
            is ResultApi.Success -> ResultApi.Success(result.data?.toDomain())
            is ResultApi.Error -> ResultApi.Error(result.message)
        }
    }

    override suspend fun getEpisodes(episodeIds: List<Int>) : ResultApi<List<EpisodeDomain>?>{
        val result = episodeService.getEpisodes(episodeIds = episodeIds)
        return when(result){
            is ResultApi.Success -> ResultApi.Success(result.data.map { it.toDomain() })
            is ResultApi.Error -> ResultApi.Error(result.message)
        }
    }

}