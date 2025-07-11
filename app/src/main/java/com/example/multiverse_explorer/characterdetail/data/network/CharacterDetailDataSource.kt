package com.example.multiverse_explorer.characterdetail.data.network

import com.example.multiverse_explorer.characterdetail.data.network.rest.model.CharacterDetailData
import com.example.multiverse_explorer.core.ResultApi

interface CharacterDetailDataSource {

    suspend fun getCharacterDetail(characterId: Int): ResultApi<CharacterDetailData?>
}