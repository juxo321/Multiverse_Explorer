package com.example.multiverse_explorer.characters.data.network.grqphql

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.example.multiverse_explorer.characters.data.mappers.toCharacterData
import com.example.multiverse_explorer.characters.data.network.CharactersDataSource
import com.example.multiverse_explorer.characters.data.network.rest.model.CharacterData
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import com.multiverse_explorer.GetCharactersQuery
import javax.inject.Inject

class CharactersGraphQLDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) : CharactersDataSource {
    override suspend fun getCharacters(selectedStatus: String): ResultApi<List<CharacterData>> =
        NetworkFunctions.safeApiCall {
            val response = apolloClient.query(GetCharactersQuery(selectedStatus = selectedStatus)).execute()
            response.data?.characters?.results?.mapNotNull { character ->
                character?.toCharacterData()
            } ?: emptyList()
        }
}