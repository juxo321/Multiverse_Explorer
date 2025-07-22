package com.example.multiverse_explorer.characterdetail.data

import android.util.Log
import com.example.multiverse_explorer.characterdetail.data.database.dao.CharacterDetailDao
import com.example.multiverse_explorer.characterdetail.data.mappers.toDomain
import com.example.multiverse_explorer.characterdetail.data.mappers.toEntity
import com.example.multiverse_explorer.characterdetail.data.network.CharacterDetailDataSource
import com.example.multiverse_explorer.characterdetail.data.network.EpisodeDataSource
import com.example.multiverse_explorer.characterdetail.data.network.graphql.CharacterDetailGraphQLDataSource
import com.example.multiverse_explorer.characterdetail.data.network.graphql.EpisodeGraphQLDataSource
import com.example.multiverse_explorer.characterdetail.data.network.rest.CharacterDetailRestDataSource
import com.example.multiverse_explorer.characterdetail.data.network.rest.EpisodeRestDataSource
import com.example.multiverse_explorer.characterdetail.data.network.rest.model.EpisodeData
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characterdetail.domain.repository.CharacterDetailRepository
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.data.datastore.SettingsDataStore
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterDetailRepositoryImp @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val characterDetailRestDataSource: CharacterDetailRestDataSource,
    private val characterDetailGraphQLDataSource: CharacterDetailGraphQLDataSource,
    private val episodeRestDataSource: EpisodeRestDataSource,
    private val episodeGraphQLDataSource: EpisodeGraphQLDataSource,
    private val characterDetailDao: CharacterDetailDao,
) : CharacterDetailRepository {

    override suspend fun getCharacterDetailFromDatabase(characterId: Int): ResultApi<CharacterDetailDomain> {
        return withContext(Dispatchers.IO) {
            characterDetailDao.getCharacterDetail(characterId = characterId)
                ?.let { ResultApi.Success(it.toDomain()) }
                ?: ResultApi.Error("Character doesn't exist")
        }
    }

    override suspend fun getCharacterDetailFromNetwork(characterId: Int): ResultApi<CharacterDetailDomain?> {
        return withContext(Dispatchers.IO) {
            val dataSource = settingsDataStore.getDataSource().first()

            val dataSourceStrategy: CharacterDetailDataSource = when(dataSource){
                "Rest" -> characterDetailRestDataSource
                "GraphQL" -> characterDetailGraphQLDataSource
                else -> characterDetailRestDataSource
            }

            NetworkFunctions.safeServiceCallCharacterDetail(
                serviceCall = { dataSourceStrategy.getCharacterDetail(characterId = characterId) },
                transform = { it?.toDomain() }
            )
        }

    }

    override suspend fun getEpisodesFromDatabase(episodeIds: List<Int>): ResultApi<List<EpisodeDomain>> {
        return withContext(Dispatchers.IO) {
            characterDetailDao.getEpisodes(episodeIds = episodeIds)
                .takeIf { it.isNotEmpty() && it.size == episodeIds.size }
                ?.let { ResultApi.Success(it.map { episode -> episode.toDomain() }) }
                ?: ResultApi.Error("Episodes don't exist")
        }
    }

    override suspend fun getEpisodesFromNetwork(episodeIds: List<Int>): ResultApi<List<EpisodeDomain>> {
        return withContext(Dispatchers.IO){

            val dataSource = settingsDataStore.getDataSource().first()

            val dataSourceStrategy: EpisodeDataSource = when(dataSource){
                "Rest" -> episodeRestDataSource
                "GraphQL" -> episodeGraphQLDataSource
                else -> episodeRestDataSource
            }


            NetworkFunctions.safeServiceCallCharacterDetail(
                serviceCall = { dataSourceStrategy.getEpisodes(episodeIds = episodeIds) },
                transform = {
                    saveEpisodesToDatabase(it)
                    it.map { episode -> episode.toDomain() }
                }
            )
        }
    }

    override suspend fun saveEpisodesToDatabase(episodes: List<EpisodeData>) {
        characterDetailDao.insertEpisodes(episodes.map { it.toEntity() })
    }


}