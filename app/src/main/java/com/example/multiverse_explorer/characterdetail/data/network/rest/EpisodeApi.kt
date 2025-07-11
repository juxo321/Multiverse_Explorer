package com.example.multiverse_explorer.characterdetail.data.network.rest

import com.example.multiverse_explorer.characterdetail.data.network.rest.model.EpisodeData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeApi {

    @GET("episode/{Ids}")
    suspend fun getEpisodes(
        @Path("Ids") episodeIds: List<Int>
    ): Response<List<EpisodeData>>
}