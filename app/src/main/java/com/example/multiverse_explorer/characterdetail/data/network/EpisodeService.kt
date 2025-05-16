package com.example.multiverse_explorer.characterdetail.data.network

import com.example.multiverse_explorer.characterdetail.data.model.EpisodeData
import com.example.multiverse_explorer.core.ResultApi
import javax.inject.Inject


class EpisodeService @Inject constructor(private val episodeApi: EpisodeApi) {

    suspend fun getEpisodes(episodeIds: List<Int>): ResultApi<List<EpisodeData>> {
        return runCatching {
            val result = episodeApi.getEpisodes(episodeIds)
            result.body() ?: emptyList()
        }.fold(
            onSuccess = { ResultApi.Success(it) },
            onFailure = { ResultApi.Error("Exception: ${it.message}") }
        )
    }
}