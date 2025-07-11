package com.example.multiverse_explorer.characterdetail.data.network.graphql

import com.apollographql.apollo.ApolloClient
import com.example.multiverse_explorer.characterdetail.data.mappers.toData
import com.example.multiverse_explorer.characterdetail.data.network.CharacterDetailDataSource
import com.example.multiverse_explorer.characterdetail.data.network.rest.model.CharacterDetailData
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import com.multiverse_explorer.GetCharacterDetailQuery
import javax.inject.Inject

class CharacterDetailGraphQLDataSource @Inject constructor(
    private val apolloClient: ApolloClient
): CharacterDetailDataSource {
    override suspend fun getCharacterDetail(characterId: Int): ResultApi<CharacterDetailData?> {
        return  NetworkFunctions.safeApiCall {
            val response = apolloClient.query(GetCharacterDetailQuery(characterId = characterId.toString())).execute()
            response.data?.character?.toData()
        }
    }
}