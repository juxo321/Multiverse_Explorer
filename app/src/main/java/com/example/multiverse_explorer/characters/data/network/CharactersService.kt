package com.example.multiverse_explorer.characters.data.network

import com.example.multiverse_explorer.characters.data.model.CharacterData
import com.example.multiverse_explorer.core.ResultApi
import javax.annotation.meta.When
import javax.inject.Inject

class CharactersService @Inject constructor(private val charactersApi: CharactersApi) {

    suspend fun getCharacters(): ResultApi<List<CharacterData>> {
        return runCatching {
            val resultApi = charactersApi.getCharacters()
            resultApi.body()?.characters ?: emptyList()
        }.fold(
            onSuccess = { ResultApi.Success(it) },
            onFailure = {ResultApi.Error("Exception: ${it.message}")
            }
        )
    }
}