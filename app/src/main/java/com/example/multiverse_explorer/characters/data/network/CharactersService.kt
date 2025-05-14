package com.example.multiverse_explorer.characters.data.network

import android.util.Log
import com.example.multiverse_explorer.characters.data.model.CharacterData
import com.example.multiverse_explorer.core.ResultApi
import javax.annotation.meta.When
import javax.inject.Inject

class CharactersService @Inject constructor(private val charactersApi: CharactersApi) {

    suspend fun getCharacters(selectedStatus: String): ResultApi<List<CharacterData>> {
        return runCatching {
            val resultApi = charactersApi.getCharacters(selectedStatus = selectedStatus)
            Log.i("RESULTTTT", resultApi.toString())
            resultApi.body()?.characters ?: emptyList()
        }.fold(
            onSuccess = { ResultApi.Success(it) },
            onFailure = {
                Log.i("AAAA", it.stackTraceToString() ?: "")
                ResultApi.Error("Exception: ${it.message}")
            }
        )
    }
}