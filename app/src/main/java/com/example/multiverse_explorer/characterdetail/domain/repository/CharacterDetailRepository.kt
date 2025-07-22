package com.example.multiverse_explorer.characterdetail.domain.repository


import com.example.multiverse_explorer.characterdetail.data.network.rest.model.EpisodeData
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.core.ResultApi

interface CharacterDetailRepository {

    suspend fun getCharacterDetailFromDatabase(characterId: Int): ResultApi<CharacterDetailDomain>

    suspend fun getCharacterDetailFromNetwork(characterId: Int): ResultApi<CharacterDetailDomain?>

    suspend fun getEpisodesFromDatabase(episodeIds: List<Int>): ResultApi<List<EpisodeDomain>>

    suspend fun getEpisodesFromNetwork(episodeIds: List<Int>) : ResultApi<List<EpisodeDomain>?>

    suspend fun saveEpisodesToDatabase(episodes: List<EpisodeData>)
}