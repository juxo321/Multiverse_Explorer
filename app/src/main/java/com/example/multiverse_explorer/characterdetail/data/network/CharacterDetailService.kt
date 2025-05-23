package com.example.multiverse_explorer.characterdetail.data.network

import android.util.Log
import com.example.multiverse_explorer.characterdetail.data.model.CharacterDetailData
import com.example.multiverse_explorer.core.ResultApi
import javax.inject.Inject

class CharacterDetailService @Inject constructor(private val characterDetailApi: CharacterDetailApi) {

    suspend fun getCharacterDetail(characterId: Int): ResultApi<CharacterDetailData?> {
        return runCatching {
            val result = characterDetailApi.getCharacterDetail(characterId)
            Log.i("RESULTTTT", result.body().toString())
            result.body()
        }.fold(
            onSuccess = { ResultApi.Success(it)},
            onFailure = {ResultApi.Error("Exception: ${it.message}")}
        )
    }
}