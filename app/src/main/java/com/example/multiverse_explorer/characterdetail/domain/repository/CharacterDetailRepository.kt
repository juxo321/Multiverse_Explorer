package com.example.multiverse_explorer.characterdetail.domain.repository


import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.core.ResultApi

interface CharacterDetailRepository {

    suspend fun getCharacterDetail(characterId: Int): ResultApi<CharacterDetailDomain?>

    suspend fun getEpisodes(episodeIds: List<Int>) : ResultApi<List<EpisodeDomain>?>
}