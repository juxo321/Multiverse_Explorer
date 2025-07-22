package com.example.multiverse_explorer.characterdetail.data.network.rest

import com.example.multiverse_explorer.characterdetail.data.network.EpisodeDataSource
import com.example.multiverse_explorer.characterdetail.data.network.rest.model.EpisodeData
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import javax.inject.Inject


class EpisodeRestDataSource @Inject constructor(private val episodeApi: EpisodeApi): EpisodeDataSource {

    override suspend fun getEpisodes(episodeIds: List<Int>): ResultApi<List<EpisodeData>> =
        NetworkFunctions.safeApiCall {
            val result = episodeApi.getEpisodes(episodeIds)
            if (result.isSuccessful) {
                result.body() ?: emptyList()
            } else {
                throw Exception(result.message())
            }
        }
}