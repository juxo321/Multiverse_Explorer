package com.example.multiverse_explorer.characterdetail.data.network.graphql

import com.apollographql.apollo.ApolloClient
import com.example.multiverse_explorer.characterdetail.data.mappers.toEpisodeData
import com.example.multiverse_explorer.characterdetail.data.network.EpisodeDataSource
import com.example.multiverse_explorer.characterdetail.data.network.rest.model.EpisodeData
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import com.multiverse_explorer.GetEpisodesQuery
import javax.inject.Inject

class EpisodeGraphQLDataSource @Inject constructor(
    private val apolloClient: ApolloClient
): EpisodeDataSource {
    override suspend fun getEpisodes(episodeIds: List<Int>): ResultApi<List<EpisodeData>> {
        return NetworkFunctions.safeApiCall {
            val response = apolloClient.query(GetEpisodesQuery(episodeIds = episodeIds.map { it.toString() })).execute()
            response.data?.episodesByIds?.mapNotNull { it?.toEpisodeData() } ?: emptyList()
        }
    }
}