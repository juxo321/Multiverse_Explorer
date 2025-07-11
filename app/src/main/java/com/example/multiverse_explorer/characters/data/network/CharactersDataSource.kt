package com.example.multiverse_explorer.characters.data.network

import com.example.multiverse_explorer.characters.data.network.rest.model.CharacterData
import com.example.multiverse_explorer.core.ResultApi

interface CharactersDataSource {
    suspend fun getCharacters(selectedStatus: String): ResultApi<List<CharacterData>>
}