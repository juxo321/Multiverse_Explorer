package com.example.multiverse_explorer.characterdetail.data.network

import android.util.Log
import com.example.multiverse_explorer.characterdetail.data.network.model.CharacterDetailData
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import javax.inject.Inject

class CharacterDetailService @Inject constructor(private val characterDetailApi: CharacterDetailApi) {

    suspend fun getCharacterDetail(characterId: Int): ResultApi<CharacterDetailData?> =
        NetworkFunctions.safeApiCall {
            val result = characterDetailApi.getCharacterDetail(characterId)
            if (result.isSuccessful) {
                result.body()
            } else {
                throw Exception(result.message())
            }

        }
}