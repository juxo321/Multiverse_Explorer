package com.example.multiverse_explorer.characters.data.network.rest

import com.example.multiverse_explorer.characters.data.network.CharactersDataSource
import com.example.multiverse_explorer.characters.data.network.rest.model.CharacterData
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import javax.inject.Inject

class CharactersRestDataSource @Inject constructor(private val charactersApi: CharactersApi): CharactersDataSource {

    override suspend fun getCharacters(selectedStatus: String): ResultApi<List<CharacterData>> =
        NetworkFunctions.safeApiCall {
            val resultApi = charactersApi.getCharacters(selectedStatus = selectedStatus)
            if(resultApi.isSuccessful){
                resultApi.body()?.characters ?: emptyList()
            }else {
                throw Exception(resultApi.message())
            }
        }
}