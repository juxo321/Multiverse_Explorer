package com.example.multiverse_explorer.characters.data.network

import android.util.Log
import com.example.multiverse_explorer.characters.data.model.CharacterData
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import javax.annotation.meta.When
import javax.inject.Inject

class CharactersService @Inject constructor(private val charactersApi: CharactersApi) {

    suspend fun getCharacters(selectedStatus: String): ResultApi<List<CharacterData>> =
        NetworkFunctions.safeApiCall {
            val resultApi = charactersApi.getCharacters(selectedStatus = selectedStatus)
            if(resultApi.isSuccessful){
                resultApi.body()?.characters ?: emptyList()
            }else {
                throw Exception(resultApi.message())
            }
        }
}