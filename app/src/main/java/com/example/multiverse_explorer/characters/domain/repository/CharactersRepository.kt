package com.example.multiverse_explorer.characters.domain.repository

import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.core.ResultApi

interface CharactersRepository {

    suspend fun getCharacters(selectedStatus: String): ResultApi<List<CharacterDomain>>
}