package com.example.multiverse_explorer.characters.domain.repository

import com.example.multiverse_explorer.characters.data.network.model.CharacterData
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.core.ResultApi
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {

    suspend fun getCharactersFromDatabase(selectedStatus: String):Flow<ResultApi<List<CharacterDomain>>>
    suspend fun getCharactersFromNetwork(selectedStatus:String)
    suspend fun saveCharactersToDatabase(charactersData: List<CharacterData>)
    suspend fun updateFavoriteCharacter(characterId: Int, isFavorite: Boolean): Int
}