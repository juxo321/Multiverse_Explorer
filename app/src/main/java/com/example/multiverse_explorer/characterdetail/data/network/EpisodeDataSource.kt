package com.example.multiverse_explorer.characterdetail.data.network

import com.example.multiverse_explorer.characterdetail.data.network.rest.model.EpisodeData
import com.example.multiverse_explorer.core.ResultApi

interface EpisodeDataSource {
    suspend fun getEpisodes(episodeIds: List<Int>): ResultApi<List<EpisodeData>>
}