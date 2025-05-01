package com.example.multiverse_explorer.characters.data

import com.example.multiverse_explorer.characters.data.mappers.toDomain
import com.example.multiverse_explorer.characters.data.network.CharactersService
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import com.example.multiverse_explorer.core.ResultApi
import javax.inject.Inject

class CharactersRepositoryImp @Inject constructor( private val charactersService: CharactersService) : CharactersRepository {

    override suspend fun getCharacters(): ResultApi<List<CharacterDomain>> {
        val result = charactersService.getCharacters()
        return when (result) {
            is ResultApi.Success -> ResultApi.Success(result.data.map { it.toDomain() })
            is ResultApi.Error -> ResultApi.Error(result.message)
        }
    }
}