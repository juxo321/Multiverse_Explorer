package com.example.multiverse_explorer.characterdetail.data.network.rest

import com.example.multiverse_explorer.characterdetail.data.network.CharacterDetailDataSource
import com.example.multiverse_explorer.characterdetail.data.network.rest.model.CharacterDetailData
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import javax.inject.Inject

class CharacterDetailRestDataSource @Inject constructor(
    private val characterDetailApi: CharacterDetailApi
): CharacterDetailDataSource {

    override suspend fun getCharacterDetail(characterId: Int): ResultApi<CharacterDetailData?> =
        NetworkFunctions.safeApiCall {
            val result = characterDetailApi.getCharacterDetail(characterId)
            if (result.isSuccessful) {
                result.body()
            } else {
                throw Exception(result.message())
            }

        }
}