package com.example.multiverse_explorer.characters.data

import com.example.multiverse_explorer.characters.data.mappers.toDomain
import com.example.multiverse_explorer.characters.data.network.CharactersService
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import javax.inject.Inject

class CharactersRepositoryImp @Inject constructor( private val charactersService: CharactersService) : CharactersRepository {

    override suspend fun getCharacters(selectedStatus:String): ResultApi<List<CharacterDomain>> =

        NetworkFunctions.safeServiceCall(
            serviceCall = { charactersService.getCharacters(selectedStatus = selectedStatus)},
            transform = { it.map { character -> character.toDomain()} }
        )
}