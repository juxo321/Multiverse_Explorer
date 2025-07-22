package com.example.multiverse_explorer.characterdetail.data

import android.util.Log
import androidx.compose.runtime.DisposableEffect
import com.example.multiverse_explorer.characterdetail.data.database.dao.CharacterDetailDao
import com.example.multiverse_explorer.characterdetail.data.mappers.toDomain
import com.example.multiverse_explorer.characterdetail.data.mappers.toEntity
import com.example.multiverse_explorer.characterdetail.data.network.model.CharacterDetailData
import com.example.multiverse_explorer.characterdetail.data.network.CharacterDetailService
import com.example.multiverse_explorer.characterdetail.data.network.EpisodeService
import com.example.multiverse_explorer.characterdetail.data.network.model.EpisodeData
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characterdetail.domain.repository.CharacterDetailRepository
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterDetailRepositoryImp @Inject constructor(
    private val characterDetailService: CharacterDetailService,
    private val characterDetailDao: CharacterDetailDao,
    private val episodeService: EpisodeService
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
            NetworkFunctions.safeServiceCallCharacterDetail(
                serviceCall = { characterDetailService.getCharacterDetail(characterId = characterId) },
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
            NetworkFunctions.safeServiceCallCharacterDetail(
                serviceCall = { episodeService.getEpisodes(episodeIds = episodeIds) },
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