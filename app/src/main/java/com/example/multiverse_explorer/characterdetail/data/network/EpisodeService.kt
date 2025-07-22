package com.example.multiverse_explorer.characterdetail.data.network

import com.example.multiverse_explorer.characterdetail.data.model.EpisodeData
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import javax.inject.Inject


class EpisodeService @Inject constructor(private val episodeApi: EpisodeApi) {

    suspend fun getEpisodes(episodeIds: List<Int>): ResultApi<List<EpisodeData>> =
        NetworkFunctions.safeApiCall {
            val result = episodeApi.getEpisodes(episodeIds)
            if (result.isSuccessful) {
                result.body() ?: emptyList()
            } else {
                throw Exception(result.message())
            }
        }
}